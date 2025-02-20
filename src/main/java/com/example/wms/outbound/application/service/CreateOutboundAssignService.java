package com.example.wms.outbound.application.service;

import com.example.wms.infrastructure.exception.DuplicatedException;
import com.example.wms.infrastructure.exception.NotFoundException;
import com.example.wms.notification.application.domain.Notification;
import com.example.wms.notification.application.port.out.NotificationPort;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.domain.OutboundPlanProduct;
import com.example.wms.outbound.application.exception.InsufficientStockException;
import com.example.wms.outbound.application.port.in.CreateOutboundAssignUseCase;
import com.example.wms.outbound.application.port.out.CreateOutboundAssignPort;
import com.example.wms.outbound.application.port.out.GetOutboundAssignPort;
import com.example.wms.product.application.domain.Product;
import com.example.wms.product.application.port.out.ProductPort;
import com.example.wms.user.application.domain.enums.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateOutboundAssignService implements CreateOutboundAssignUseCase {

    private final CreateOutboundAssignPort createOutboundAssignPort;
    private final NotificationPort notificationPort;
    private final GetOutboundAssignPort getOutboundAssignPort;
    private final ProductPort productPort;

    @Override
    @Transactional
    public Notification createOutboundAssign(Long outboundPlanId) {
        // 1. 기존 출고 정보 확인
        Outbound existingOutbound = createOutboundAssignPort.findOutboundByPlanId(outboundPlanId);

        if (existingOutbound != null && existingOutbound.getOutboundAssignNumber() != null) {
            // 이미 출고 지시가 있음
            throw new DuplicatedException("이미 출고 지시가 등록된 상태입니다.");
        }

        List<OutboundPlanProduct> outboundPlanProducts = createOutboundAssignPort.findOutboundPlanProductsByPlanId(outboundPlanId);

        // 3. 각 상품별 재고 확인 및 차감
        for (OutboundPlanProduct planProduct : outboundPlanProducts) {
            Long productId = planProduct.getProductId();
            int requiredQuantity = planProduct.getRequiredQuantity();

            // product 조회
            Product product = productPort.findById(productId);

            if (product == null) {
                throw new NotFoundException("상품 정보를 찾을 수 없습니다. 상품ID: " + productId);
            }

            // 재고 계산
            int lotUnit = product.getLotUnit();
            int stockLotCount = product.getStockLotCount();
            int stockQuantity = stockLotCount * lotUnit;

            // 재고 체크
            if (stockQuantity < requiredQuantity) {
                throw new InsufficientStockException("재고가 부족합니다. 상품: " + product.getProductName()
                        + ", 필요수량: " + requiredQuantity + ", 현재재고: " + stockQuantity);
            }

            // 재고 차감
            int requiredLotCount = requiredQuantity / lotUnit;

            // 재고 업데이트
            productPort.updateRequiredQuantity(productId, -requiredLotCount);

            log.info("🍳 product 재고 업데이트 완료");
        }

        String currentDate = LocalDate.now().toString().replace("-", "");
        String maxOutboundAssignNumber = createOutboundAssignPort.findMaxOutboundAssignNumber();
        String nextNumber = "0000";

        if (maxOutboundAssignNumber != null) {
            String lastNumberStr = maxOutboundAssignNumber.substring(maxOutboundAssignNumber.length() - 4);
            int lastNumber = Integer.parseInt(lastNumberStr);
            nextNumber = String.format("%04d", lastNumber + 1);
        }

        String oaNumber = "OA" + currentDate + nextNumber;

        if (existingOutbound != null) {
            // null값들 업데이트하기
            existingOutbound.setOutboundAssignNumber(oaNumber);
            existingOutbound.setOutboundAssignDate(LocalDate.now());

            createOutboundAssignPort.update(existingOutbound);
        } else {
            // 새로운 출고 정보 저장
            Outbound outbound = Outbound.builder()
                    .outboundPlanId(outboundPlanId)
                    .outboundAssignNumber(oaNumber)
                    .outboundAssignDate(LocalDate.now())
                    .outboundPickingNumber(null)
                    .outboundPickingDate(null)
                    .outboundPackingNumber(null)
                    .outboundPackingDate(null)
                    .outboundLoadingNumber(null)
                    .outboundLoadingDate(null)
                    .build();

            createOutboundAssignPort.save(outbound);
        }

        // outboundPlan status 바꿔주기
        OutboundPlan outboundPlan = getOutboundAssignPort.findOutboundPlanByOutboundPlanId(outboundPlanId);

        // outboundPlanID로 outboundPlanProduct찾아서 productId가져와서 product테이블 찾아서 재고체크 후 requiredQuantity 빼주기
        // stock_lot_count = (requiredQuantity / lot_unit)

        createOutboundAssignPort.updateOutboundPlanStatus(outboundPlan);

        Notification notification = Notification.builder()
                .content("출고 지시가 등록되었습니다.")
                .event("출고 지시")
                .userRole(UserRole.ROLE_ADMIN)
                .build();

        notificationPort.save(notification);
        return notification;
    }
}

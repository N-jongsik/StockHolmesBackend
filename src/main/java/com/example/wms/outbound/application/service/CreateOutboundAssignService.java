package com.example.wms.outbound.application.service;

import com.example.wms.infrastructure.exception.DuplicatedException;
import com.example.wms.infrastructure.exception.NotFoundException;
import com.example.wms.notification.application.domain.Notification;
import com.example.wms.notification.application.port.out.NotificationPort;
import com.example.wms.outbound.adapter.in.dto.OutboundLotDTO;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.domain.OutboundPlanProduct;
import com.example.wms.outbound.application.exception.InsufficientStockException;
import com.example.wms.outbound.application.port.in.CreateOutboundAssignUseCase;
import com.example.wms.outbound.application.port.out.CreateOutboundAssignPort;
import com.example.wms.outbound.application.port.out.GetOutboundAssignPort;
import com.example.wms.product.application.domain.Bin;
import com.example.wms.product.application.domain.Lot;
import com.example.wms.product.application.domain.Product;
import com.example.wms.product.application.port.out.BinPort;
import com.example.wms.product.application.port.out.LotPort;
import com.example.wms.product.application.port.out.ProductPort;
import com.example.wms.user.application.domain.enums.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateOutboundAssignService implements CreateOutboundAssignUseCase {

    private final CreateOutboundAssignPort createOutboundAssignPort;
    private final NotificationPort notificationPort;
    private final GetOutboundAssignPort getOutboundAssignPort;
    private final ProductPort productPort;
    private final LotPort lotPort;
    private final BinPort binPort;

    @Override
    @Transactional
    public Notification createOutboundAssign(String worker,Long outboundPlanId) {
        // 기존 출고 정보 확인
        Outbound existingOutbound = createOutboundAssignPort.findOutboundByPlanId(outboundPlanId);

        if (existingOutbound != null && existingOutbound.getOutboundAssignNumber() != null) {
            // 이미 출고 지시가 있음
            throw new DuplicatedException("이미 출고 지시가 등록된 상태입니다.");
        }

        List<OutboundPlanProduct> outboundPlanProducts = createOutboundAssignPort.findOutboundPlanProductsByPlanId(outboundPlanId);

        // 각 상품별 재고 확인 및 차감
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

        // Outbound 생성 또는 업데이트
        Outbound outbound = createOrUpdateOutbound(outboundPlanId, existingOutbound);

        // 저장된 Outbound 정보로 Lot 처리 로직 수행
        processLotsInternal(worker, outboundPlanProducts, outbound.getOutboundId());

        // outboundPlan status 바꿔주기
        OutboundPlan outboundPlan = getOutboundAssignPort.findOutboundPlanByOutboundPlanId(outboundPlanId);
        createOutboundAssignPort.updateOutboundPlanStatus(outboundPlan);

        Notification notification = Notification.builder()
                .content("출고 지시가 등록되었습니다.")
                .event("출고 지시")
                .userRole(UserRole.ROLE_ADMIN)
                .build();

        notificationPort.save(notification);
        return notification;
    }

    private Outbound createOrUpdateOutbound(Long outboundPlanId, Outbound existingOutbound) {
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
            return existingOutbound;
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
            return outbound;
        }
    }

    private List<OutboundLotDTO> processLotsInternal(String worker, List<OutboundPlanProduct> outboundPlanProducts, Long outboundId) {
        List<OutboundLotDTO.LotLocation> allLotLocations = new ArrayList<>();
        Outbound outbound = getOutboundAssignPort.findOutboundByOutboundId(outboundId);

        for (OutboundPlanProduct planProduct : outboundPlanProducts) {
            Long productId = planProduct.getProductId();
            int requiredQuantity = planProduct.getRequiredQuantity();
            Product product = productPort.findById(productId);
            int lotUnit = product.getLotUnit();
            int requiredLotCount = requiredQuantity / lotUnit;

            List<Lot> lots;

            if(worker.equals("create")) {
                lots = lotPort.findLotsByProductId(productId, requiredLotCount);
            } else{
                lots = lotPort.findLotsSupplierByProductId(productId, requiredLotCount);
            }

            if (lots == null || lots.isEmpty()) {
                log.warn("❌ 제품 ID {}에 대한 로트를 찾을 수 없습니다", productId);
                continue;
            }

            // lotId 리스트 생성
            List<Long> lotIds = lots.stream()
                    .map(Lot::getLotId)
                    .collect(Collectors.toList());

            if(worker.equals("create")) {
                lotPort.updateOutboundIdForLots(lotIds, outboundId);
            }

            // 정렬 없이 위치 정보만 생성
            List<OutboundLotDTO.LotLocation> productLotLocations = lots.stream()
                    .map(lot -> {
                        Bin bin = binPort.findBinByBinId(lot.getBinId());
                        return OutboundLotDTO.LotLocation.builder()
                                .lotId(lot.getLotId())
                                .binId(lot.getBinId())
                                .binCode(bin.getBinCode())
                                .zone(bin.getZone())
                                .aisle(bin.getAisle())
                                .rowNum(bin.getRowNum())
                                .floor(bin.getFloor())
                                .productName(product.getProductName())
                                .productCode(product.getProductCode())
                                .build();
                    })
                    .collect(Collectors.toList());

            // 모든 제품의 위치 정보를 하나의 리스트에 추가
            allLotLocations.addAll(productLotLocations);

            log.info("🎁 Lot 업데이트 완료 - ProductId: {}, OutboundId: {}, LotCount: {}",
                    productId, outboundId, lots.size());
        }

        // 전체 리스트에 대해 binId 기준으로 정렬
        List<OutboundLotDTO.LotLocation> sortedLotLocations = allLotLocations.stream()
                .sorted(Comparator.comparing(OutboundLotDTO.LotLocation::getBinId))
                .collect(Collectors.toList());

        // 최종적으로 정렬된 위치 정보가 포함된 단일 DTO 반환
        return Collections.singletonList(OutboundLotDTO.builder()
                .outboundId(outboundId)
                .outboundAssignNumber(outbound.getOutboundAssignNumber())
                .lotLocations(sortedLotLocations)
                .build());
    }

    @Override
    @Transactional
    public List<OutboundLotDTO> processCurrentDayLots(String worker) {
        LocalDate today = LocalDate.now();
        System.out.println("🤡 scheduleDate = " + today); // 2025-02-21이 출력되는지 확인

        // 오늘 날짜와 일치하는 outbound 조회
        List<Outbound> todayOutbounds = getOutboundAssignPort.findOutboundsByScheduleDate(today);

        if (todayOutbounds.isEmpty()) {
            log.info("📅 오늘 날짜({})의 출고 예정 데이터가 없습니다.", today);
            return new ArrayList<>();
        }

        List<OutboundLotDTO> allResults = new ArrayList<>();

        for (Outbound outbound : todayOutbounds) {
            Long outboundId = outbound.getOutboundId();
            Long outboundPlanId = outbound.getOutboundPlanId();

            System.out.println("🪥 "+outboundPlanId);

            // 출고 계획 상품 조회
            List<OutboundPlanProduct> planProducts =
                    createOutboundAssignPort.findOutboundPlanProductsByPlanId(outboundPlanId);

            System.out.println("🌭 출고 계획 상품 조회" + planProducts);

            // 로트 처리 수행
            List<OutboundLotDTO> outboundLots = processLotsInternal(worker, planProducts, outboundId);
            allResults.addAll(outboundLots);
        }

        return allResults;
    }
}
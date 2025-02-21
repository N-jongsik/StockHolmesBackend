package com.example.wms.outbound.application.service;

import com.example.wms.inventory.application.port.out.InventoryPort;
import com.example.wms.notification.application.domain.Notification;
import com.example.wms.notification.application.port.out.NotificationPort;
import com.example.wms.order.application.domain.Order;
import com.example.wms.order.application.domain.OrderProduct;
import com.example.wms.order.application.port.out.RegisterOrderPort;
import com.example.wms.order.application.port.out.RegisterOrderProductPort;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.domain.OutboundPlanProduct;
import com.example.wms.outbound.application.port.in.CreateOutboundPlanProductUseCase;
import com.example.wms.outbound.application.port.out.CreateOutboundPlanProductPort;
import com.example.wms.outbound.application.port.out.GetOutboundPlanPort;
import com.example.wms.product.application.domain.Product;
import com.example.wms.product.application.port.out.ProductPort;
import com.example.wms.user.application.domain.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CreateOutboundPlanProductService implements CreateOutboundPlanProductUseCase {
    private final CreateOutboundPlanProductPort createOutboundPlanProductPort;
    private final GetOutboundPlanPort getOutboundPlanPort;
    private final InventoryPort inventoryPort;
    private final RegisterOrderPort registerOrderPort;
    private final RegisterOrderProductPort registerOrderProductPort;
    private final NotificationPort notificationPort;
    private final ProductPort productPort;

    @Override
    public Notification createOutboundPlanProduct(Long outboundPlanId, List<ProductInfoDto> productInfoDtoList) {

        // 재고 check
        OutboundPlan outboundPlan = getOutboundPlanPort.findOutboundPlanByOutboundPlanId(outboundPlanId)
                .orElseThrow(() -> new IllegalArgumentException("outboundPlanId가 없습니다. "));

        List<OutboundPlanProduct> outboundPlanProductList = new ArrayList<>();
        List<OrderProduct> orderProductList = new ArrayList<>();

        for (ProductInfoDto dto : productInfoDtoList) {
            Order order = null;

            Long productId = dto.getProductId();
            Product product = productPort.findById(productId);

            String currentDate = LocalDate.now().toString().replace("-", "");
            String maxOrderNumber = registerOrderPort.findMaxOutboundOrderNumber();
            String nextNumber = "0000";

            if (maxOrderNumber != null) {
                String lastNumberStr = maxOrderNumber.substring(maxOrderNumber.length() - 4);
                int lastNumber = Integer.parseInt(lastNumberStr);
                nextNumber = String.format("%04d", lastNumber + 1);
            }

            String orNumber = "OR" + currentDate + nextNumber;

            Integer availableQuantity = inventoryPort.findAvailableQuantityByProductId(productId);
            Integer requiredQuantity = dto.getProductCount();

            Integer stockUsedQuantity = requiredQuantity > availableQuantity ? 0 : requiredQuantity;
            Integer orderQuantity = requiredQuantity > availableQuantity ? requiredQuantity : 0;

            // 재고 감소 처리
            if (stockUsedQuantity > 0) {
                inventoryPort.updateInventoryAvailableQuantity(productId, availableQuantity - stockUsedQuantity);
            }

            // 발주 필요 시 Order 생성
            if (orderQuantity > 0) {
                order = Order.builder()
                        .supplierId(product.getSupplierId())
                        .orderDate(LocalDate.now())
                        .inboundDate(LocalDate.now().plusDays(product.getLeadTime()))
                        .isApproved(false)
                        .isDelayed(false)
                        .orderNumber(orNumber)
                        .orderStatus("처리중")
                        .dailyPlanId(outboundPlan.getOutboundPlanId())
                        .isReturnOrder(false)
                        .build();
                registerOrderPort.saveOrder(order);

                orderProductList.add(OrderProduct.builder()
                        .orderId(order.getOrderId())
                        .productId(productId)
                        .productCount(orderQuantity)
                        .isDefective(false)
                        .productName(product.getProductName())
                        .build());
            }

            // OutboundPlanProduct 생성
            outboundPlanProductList.add(OutboundPlanProduct.builder()
                    .outboundPlanId(outboundPlanId)
                    .productId(productId)
                    .requiredQuantity(requiredQuantity)
                    .stockUsedQuantity(stockUsedQuantity)
                    .orderQuantity(orderQuantity)
                    .status("진행중")
                    .build());
        }

        createOutboundPlanProductPort.saveAll(outboundPlanProductList);

        // OrderProduct 저장
        if (!orderProductList.isEmpty()) {
            registerOrderProductPort.saveAll(orderProductList);
        }

        // 알림 저장 및 전송
        Notification notification = Notification.builder()
                .content("출고 예정이 등록되었습니다.")
                .event("출고 예정")
                .userRole(UserRole.ROLE_ADMIN)
                .build();
        notificationPort.save(notification);

        return notification;
    }
}

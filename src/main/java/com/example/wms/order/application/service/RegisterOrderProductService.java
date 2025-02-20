package com.example.wms.order.application.service;

import com.example.wms.inbound.application.domain.Inbound;
import com.example.wms.inbound.application.port.out.AssignInboundNumberPort;
import com.example.wms.inbound.application.port.out.InboundPort;
import com.example.wms.notification.application.domain.Notification;
import com.example.wms.notification.application.port.out.NotificationPort;
import com.example.wms.order.adapter.in.dto.ProductListDto;
import com.example.wms.order.application.domain.OrderProduct;
import com.example.wms.order.application.port.in.RegisterOrderProductUseCase;
import com.example.wms.order.application.port.out.RegisterOrderProductPort;
import com.example.wms.user.application.domain.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegisterOrderProductService implements RegisterOrderProductUseCase {

    private final RegisterOrderProductPort registerOrderProductPort;
    private final NotificationPort notificationPort;

    @Override
    public Notification registerOrderProduct(Long orderId, List<ProductListDto> productListDtos) {
        List<OrderProduct> orderProductList = productListDtos.stream()
                .map(dto -> OrderProduct.builder()
                        .orderId(orderId)
                        .productId(dto.getProductId())
                        .productCount(dto.getProductCount())
                        .productName(dto.getProductName())
                        .isDefective(null)
                        .build())
                .collect(Collectors.toList());

        registerOrderProductPort.saveAll(orderProductList);

        Notification notification = Notification.builder()
                .content("발주가 등록되었습니다.")
                .event("발주 등록")
                .userRole(UserRole.ROLE_ADMIN)
                .build();

        notificationPort.save(notification);
        return notification;
    }
}

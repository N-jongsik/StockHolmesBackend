package com.example.wms.order.adapter.out;

import com.example.wms.inbound.application.domain.Inbound;
import com.example.wms.infrastructure.exception.NotFoundException;
import com.example.wms.infrastructure.mapper.*;
import com.example.wms.order.application.domain.Order;
import com.example.wms.order.application.domain.OrderProduct;
import com.example.wms.order.application.port.out.OrderPort;
import com.example.wms.order.application.port.out.OrderProductPort;
import com.example.wms.outbound.adapter.in.dto.OutboundPlanRequestDto;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.out.CreateOutboundPlanPort;
import com.example.wms.product.application.domain.Product;
import com.example.wms.product.application.port.out.ProductPort;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderAdapter implements OrderPort {

    private final ProductPort productPort;
    private final OrderProductPort orderProductPort;
    private final OrderMapper orderMapper;
    private final InboundMapper inboundMapper;

    @Override
    public Long createOrder(Long productId, Long inboundId, Long defectiveCount) {

        Product product = productPort.findById(productId);

        if (product == null) {
            throw new NotFoundException("Product not found for ID : " + productId);
        }


        String orderNumber = generateOrderNumber();

            Order order = Order.builder()
                    .dailyPlanId(orderMapper.findOrderById(inboundMapper.findById(inboundId).getOrderId()).getDailyPlanId())
                    .supplierId(product.getSupplierId())
                    .orderNumber(orderNumber)
                    .orderDate(LocalDate.now())
                    .isApproved(true)
                    .isReturnOrder(true)
                    .orderStatus("처리중")
                    .inboundDate(LocalDate.now())
                    .build();
            orderMapper.createOrder(order);
            Long returnOrderId = order.getOrderId();
        return returnOrderId;
    }

    @Override
    public Long createOrderWithSupplier(Long supplierId, Long inboundId) {


        Order order = orderMapper.findOrderById(inboundMapper.findById(inboundId).getOrderId());

        String orderNumber = generateOrderNumber();

        Order newOrder = Order.builder()
                .dailyPlanId(order.getDailyPlanId())
                .supplierId(supplierId)
                .orderNumber(orderNumber)
                .orderDate(LocalDate.now())
                .isApproved(true)
                .isReturnOrder(true)
                .orderStatus("처리중")
                .inboundDate(LocalDate.now())
                .build();

        orderMapper.createOrder(newOrder);
        return newOrder.getOrderId();

    }

    private String generateOrderNumber() {
        String prefix = "OR" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String lastOrderNumber = orderMapper.getLastOrderNumber();

        int newNumber = 0;

        if (lastOrderNumber != null) {
            String lastNumberStr = lastOrderNumber.substring(10);
            newNumber = Integer.parseInt(lastNumberStr) + 1;
        }

        return prefix + String.format("%04d", newNumber);
    }
}

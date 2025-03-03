package com.example.wms.order.application.service;

import com.example.wms.order.adapter.in.dto.OrderRequestDto;
import com.example.wms.order.application.domain.Order;
import com.example.wms.order.application.port.in.RegisterOrderUseCase;
import com.example.wms.order.application.port.out.RegisterOrderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RegisterOrderService implements RegisterOrderUseCase {

    private final RegisterOrderPort registerOrderPort;

    @Override
    public Long registerOrder(OrderRequestDto orderRequestDto) {

        String currentDate = LocalDate.now().toString().replace("-", "");
        String maxOrderNumber = registerOrderPort.findMaxOutboundOrderNumber();
        String nextNumber = "0000";

        if (maxOrderNumber != null) {
            String lastNumberStr = maxOrderNumber.substring(maxOrderNumber.length() - 4);
            int lastNumber = Integer.parseInt(lastNumberStr);
            nextNumber = String.format("%04d", lastNumber + 1);
        }

        String orNumber = "OR" + currentDate + nextNumber;

        Order order = Order.builder()
                .supplierId(orderRequestDto.getSupplierId())
                .orderDate(LocalDate.now())
                .inboundDate(null)
                .isApproved(false)
                .isDelayed(null)
                .orderNumber(orNumber)
                .orderStatus("처리중")
                .dailyPlanId(orderRequestDto.getOutboundPlanId())
                .isReturnOrder(false) // 일단은 false
                .build();

        registerOrderPort.saveOrder(order);

        return order.getOrderId();
    }
}

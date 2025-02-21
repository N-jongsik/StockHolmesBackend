package com.example.wms.order.adapter.out;

import com.example.wms.infrastructure.mapper.OrderMapper;
import com.example.wms.order.application.domain.Order;
import com.example.wms.order.application.port.out.RegisterOrderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterOrderAdapter implements RegisterOrderPort {

    private final  OrderMapper orderMapper;

    @Override
    public void saveOrder(Order order) {
        orderMapper.registerOrder(order);
    }

    @Override
    public String findMaxOutboundOrderNumber() {
        return orderMapper.findMaxOutboundOrderNumber();
    }
}

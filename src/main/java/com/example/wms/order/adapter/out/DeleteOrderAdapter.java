package com.example.wms.order.adapter.out;

import com.example.wms.infrastructure.mapper.OrderMapper;
import com.example.wms.order.application.port.out.DeleteOrderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteOrderAdapter implements DeleteOrderPort {

    private final OrderMapper orderMapper;

    @Override
    public void deleteOrderOrderProduct(Long orderId) {
        orderMapper.deleteOrderOrderProduct(orderId);
    }
}

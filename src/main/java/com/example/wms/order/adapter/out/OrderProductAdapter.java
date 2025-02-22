package com.example.wms.order.adapter.out;

import com.example.wms.infrastructure.mapper.OrderProductMapper;
import com.example.wms.order.application.domain.OrderProduct;
import com.example.wms.order.application.port.out.OrderProductPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderProductAdapter implements OrderProductPort {

    private final OrderProductMapper orderProductMapper;

    @Override
    public OrderProduct findByProductId(Long productId) {
        return orderProductMapper.findByProductId(productId);
    }

    @Override
    public void save(OrderProduct orderProduct) {
        orderProductMapper.save(orderProduct);
    }

    @Override
    public OrderProduct findByOrderId(Long orderId, Long productId) {
       return  orderProductMapper.findByOrderId(orderId, productId);
    }
}

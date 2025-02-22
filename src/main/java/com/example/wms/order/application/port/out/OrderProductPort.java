package com.example.wms.order.application.port.out;

import com.example.wms.order.application.domain.OrderProduct;

import java.util.List;

public interface OrderProductPort {
    OrderProduct findByProductId(Long productId);
    void save(OrderProduct orderProduct);
    OrderProduct findByOrderId(Long orderId, Long productId);
}

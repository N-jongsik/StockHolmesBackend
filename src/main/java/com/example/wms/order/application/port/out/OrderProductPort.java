package com.example.wms.order.application.port.out;

import com.example.wms.order.application.domain.OrderProduct;

public interface OrderProductPort {
    OrderProduct findByProductId(Long productId);
    void save(OrderProduct orderProduct);
}

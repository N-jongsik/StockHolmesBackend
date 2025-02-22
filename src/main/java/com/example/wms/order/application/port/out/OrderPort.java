package com.example.wms.order.application.port.out;

import com.example.wms.order.application.domain.Order;

public interface OrderPort {
    Long createOrder(Long productId, Long inboundId, Long defectiveCount);
}

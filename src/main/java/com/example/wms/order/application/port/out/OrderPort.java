package com.example.wms.order.application.port.out;

public interface OrderPort {
    Long createOrder(Long productId, Long inboundId, Long defectiveCount);
}

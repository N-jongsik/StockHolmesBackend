package com.example.wms.order.application.port.out;

public interface OrderPort {
    void createOrder(Long productId, Long inboundId, Long defectiveCount);
}

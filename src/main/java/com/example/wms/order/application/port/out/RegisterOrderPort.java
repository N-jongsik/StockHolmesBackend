package com.example.wms.order.application.port.out;

import com.example.wms.order.application.domain.Order;

public interface RegisterOrderPort {
    void saveOrder(Order order);
    String findMaxOutboundOrderNumber();
}

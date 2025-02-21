package com.example.wms.order.application.port.out;

import com.example.wms.order.application.domain.OrderProduct;

import java.util.List;

public interface RegisterOrderProductPort {
    void saveAll(List<OrderProduct> orderProducts);
}

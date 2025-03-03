package com.example.wms.order.application.port.out;

import com.example.wms.order.adapter.in.dto.ProductListDto;

import java.util.List;

public interface UpdateOrderPort {
    void updateOrder(Long orderId, List<ProductListDto> productList);
    void updateOrderApprove(Long orderId);
}

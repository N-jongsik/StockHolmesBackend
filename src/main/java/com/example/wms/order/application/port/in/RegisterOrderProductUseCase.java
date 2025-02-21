package com.example.wms.order.application.port.in;

import com.example.wms.notification.application.domain.Notification;
import com.example.wms.order.adapter.in.dto.ProductListDto;

import java.util.List;

public interface RegisterOrderProductUseCase {
    Notification registerOrderProduct(Long orderId, List<ProductListDto> productListDtos);
}

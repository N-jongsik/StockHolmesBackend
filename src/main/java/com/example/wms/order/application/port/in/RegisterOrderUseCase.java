package com.example.wms.order.application.port.in;

import com.example.wms.order.adapter.in.dto.OrderRequestDto;

public interface RegisterOrderUseCase {
    Long registerOrder(OrderRequestDto orderRequestDto);
}

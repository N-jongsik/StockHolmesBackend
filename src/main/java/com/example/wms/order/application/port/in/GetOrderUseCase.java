package com.example.wms.order.application.port.in;

import com.example.wms.order.adapter.in.dto.OrderResponseDto;
import com.example.wms.order.application.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface GetOrderUseCase {
    Order getOrder(Long orderId);
    Page<OrderResponseDto> getFilteredOrder(String orderNumber, LocalDate startDate, LocalDate endDate, Pageable pageable);
    Page<OrderResponseDto> getFilteredOrderWithSupplier(Long supplierId,String orderNumber, LocalDate startDate, LocalDate endDate, Pageable pageable);
}

package com.example.wms.order.application.port.out;

import com.example.wms.order.adapter.in.dto.OrderResponseDto;
import com.example.wms.order.adapter.in.dto.ProductListDto;
import com.example.wms.order.application.domain.Order;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface GetOrderPort {
    Order getOrder(Long orderId);
    List<OrderResponseDto> findOrderFilteringWithPageNation(String orderNumber, LocalDate startDate, LocalDate endDate, Pageable pageable);
    Integer countAllOrder(String orderNumber, LocalDate startDate, LocalDate endDate);
    List<OrderResponseDto> findOrderSupplierFilteringWithPageNation(Long supplierId, String orderNumber, LocalDate startDate, LocalDate endDate, Pageable pageable);
    Integer countAllOrderSupplier(Long supplierId, String orderNumber, LocalDate startDate, LocalDate endDate);
}

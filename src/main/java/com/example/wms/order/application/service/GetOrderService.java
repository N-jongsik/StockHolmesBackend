package com.example.wms.order.application.service;

import com.example.wms.infrastructure.pagination.util.PageableUtils;
import com.example.wms.order.adapter.in.dto.OrderResponseDto;
import com.example.wms.order.application.domain.Order;
import com.example.wms.order.application.port.in.GetOrderUseCase;
import com.example.wms.order.application.port.out.GetOrderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetOrderService implements GetOrderUseCase {

    private final GetOrderPort getOrderPort;

    @Override
    public Order getOrder(Long orderId) {
        return getOrderPort.getOrder(orderId);
    }

    @Override
    public Page<OrderResponseDto> getFilteredOrder(String orderNumber, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Pageable safePageable = PageableUtils.convertToSafePageableStrict(pageable, OrderResponseDto.class);
        List<OrderResponseDto> orderList = getOrderPort.findOrderFilteringWithPageNation(orderNumber, startDate, endDate, safePageable);
        Integer count = getOrderPort.countAllOrder(orderNumber, startDate, endDate);
        return new PageImpl<>(orderList, pageable, count);
    }

    @Override
    public Page<OrderResponseDto> getFilteredOrderWithSupplier(Long supplierId, String orderNumber, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Pageable safePageable = PageableUtils.convertToSafePageableStrict(pageable, OrderResponseDto.class);
        List<OrderResponseDto> orderList = getOrderPort.findOrderSupplierFilteringWithPageNation(supplierId, orderNumber, startDate, endDate, safePageable);
        Integer count = getOrderPort.countAllOrderSupplier(supplierId, orderNumber, startDate, endDate);
        return new PageImpl<>(orderList, pageable, count);
    }
}

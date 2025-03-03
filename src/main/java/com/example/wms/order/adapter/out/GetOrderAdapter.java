package com.example.wms.order.adapter.out;

import com.example.wms.infrastructure.mapper.OrderMapper;
import com.example.wms.order.adapter.in.dto.OrderResponseDto;
import com.example.wms.order.adapter.in.dto.ProductListDto;
import com.example.wms.order.application.domain.Order;
import com.example.wms.order.application.port.out.GetOrderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetOrderAdapter implements GetOrderPort {

    private final OrderMapper orderMapper;

    @Override
    public Order getOrder(Long orderId) {
        return orderMapper.findOrderById(orderId);
    }

    @Override
    public List<OrderResponseDto> findOrderFilteringWithPageNation(String orderNumber, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return orderMapper.findOrderFilteringWithPageNation(orderNumber, startDate, endDate, pageable);
    }

    @Override
    public Integer countAllOrder(String orderNumber, LocalDate startDate, LocalDate endDate) {
        return orderMapper.countAllOrder(orderNumber, startDate, endDate);
    }

    @Override
    public List<OrderResponseDto> findOrderSupplierFilteringWithPageNation(Long supplierId, String orderNumber, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return orderMapper.findOrderSupplierFilteringWithPageNation(supplierId, orderNumber, startDate, endDate, pageable);
    }

    @Override
    public Integer countAllOrderSupplier(Long supplierId, String orderNumber, LocalDate startDate, LocalDate endDate) {
        return orderMapper.countAllOrderSupplier(supplierId, orderNumber, startDate, endDate);
    }
}

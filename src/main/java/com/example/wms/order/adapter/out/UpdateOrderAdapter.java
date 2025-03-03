package com.example.wms.order.adapter.out;

import com.example.wms.infrastructure.mapper.OrderMapper;
import com.example.wms.order.adapter.in.dto.ProductListDto;
import com.example.wms.order.application.port.out.UpdateOrderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UpdateOrderAdapter implements UpdateOrderPort {

    private final OrderMapper orderMapper;

    @Override
    public void updateOrder(Long orderId, List<ProductListDto> productList) {
        orderMapper.deleteOrderProduct(orderId);
        orderMapper.upDateOrderProducts(orderId, productList);
    }

    @Override
    public void updateOrderApprove(Long orderId) {
        orderMapper.upDateOrderApprove(orderId);
    }
}

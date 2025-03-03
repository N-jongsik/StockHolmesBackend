package com.example.wms.order.adapter.out;

import com.example.wms.infrastructure.mapper.OrderProductMapper;
import com.example.wms.order.application.domain.OrderProduct;
import com.example.wms.order.application.port.out.RegisterOrderProductPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RegisterOrderProductAdapter implements RegisterOrderProductPort {

    private final OrderProductMapper orderProductMapper;

    @Override
    public void saveAll(List<OrderProduct> orderProducts) {
        orderProductMapper.batchInsert(orderProducts);
    }
}

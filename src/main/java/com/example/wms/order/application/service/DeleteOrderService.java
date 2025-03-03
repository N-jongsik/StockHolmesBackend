package com.example.wms.order.application.service;

import com.example.wms.order.application.port.in.DeleteOrderUseCase;
import com.example.wms.order.application.port.out.DeleteOrderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteOrderService implements DeleteOrderUseCase {

    private final DeleteOrderPort deleteOrderPort;

    @Override
    public void deleteOrder(Long orderId) {
        deleteOrderPort.deleteOrderOrderProduct(orderId);
    }
}

package com.example.wms.outbound.application.service;

import com.example.wms.outbound.application.port.in.DeleteOutboundPlanProductUseCase;
import com.example.wms.outbound.application.port.out.DeleteOutboundPlanProductPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteOutboundPlanProductService implements DeleteOutboundPlanProductUseCase {

    private final DeleteOutboundPlanProductPort deleteOutboundPlanProductPort;

    @Override
    public void deleteOutboundPlanProduct(Long outboundPlanId) {
        deleteOutboundPlanProductPort.deleteOutboundPlanProduct(outboundPlanId);
    }
}

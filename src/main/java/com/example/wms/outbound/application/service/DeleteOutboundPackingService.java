package com.example.wms.outbound.application.service;

import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.in.DeleteOutboundPackingUseCase;
import com.example.wms.outbound.application.port.out.DeleteOutboundPackingPort;
import com.example.wms.outbound.application.port.out.DeleteOutboundPickingPort;
import com.example.wms.outbound.application.port.out.GetOutboundPackingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteOutboundPackingService implements DeleteOutboundPackingUseCase {

    private final DeleteOutboundPackingPort deleteOutboundPackingPort;
    private final GetOutboundPackingPort getOutboundPackingPort;

    @Override
    public void deleteOutboundPacking(Long outboundId) {
        Outbound outbound = getOutboundPackingPort.findOutboundByOutboundId(outboundId);
        OutboundPlan outboundPlan = getOutboundPackingPort.findOutboundPlanByOutboundPlanId(outbound.getOutboundPlanId());
        deleteOutboundPackingPort.updateOutboundPlanStatus(outboundPlan);
        deleteOutboundPackingPort.deleteOutboundPacking(outboundId);
    }
}

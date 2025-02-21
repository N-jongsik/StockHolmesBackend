package com.example.wms.outbound.application.service;

import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.in.DeleteOutboundPickingUseCase;
import com.example.wms.outbound.application.port.out.DeleteOutboundPickingPort;
import com.example.wms.outbound.application.port.out.GetOutboundPickingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteOutboundPickingService implements DeleteOutboundPickingUseCase {

    private final DeleteOutboundPickingPort deleteOutboundPickingPort;
    private final GetOutboundPickingPort getOutboundPickingPort;

    @Override
    public void deleteOutboundPicking(Long outboundId) {
        Outbound outbound = getOutboundPickingPort.findOutboundByOutboundId(outboundId);
        OutboundPlan outboundPlan = getOutboundPickingPort.findOutboundPlanByOutboundPlanId(outbound.getOutboundPlanId());
        deleteOutboundPickingPort.updateOutboundPlanStatus(outboundPlan);
        deleteOutboundPickingPort.deleteOutboundPicking(outboundId);
    }
}

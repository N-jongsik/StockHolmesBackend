package com.example.wms.outbound.application.service;

import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.in.DeleteOutboundAssignUseCase;
import com.example.wms.outbound.application.port.out.DeleteOutboundAssignPort;
import com.example.wms.outbound.application.port.out.GetOutboundAssignPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteOutboundAssignService implements DeleteOutboundAssignUseCase {

    private final DeleteOutboundAssignPort deleteOutboundAssignPort;
    private final GetOutboundAssignPort getOutboundAssignPort;

    @Override
    public void deleteOutboundAssign(Long outboundId) {
        Outbound outbound = getOutboundAssignPort.findOutboundByOutboundId(outboundId);
        OutboundPlan outboundPlan = getOutboundAssignPort.findOutboundPlanByOutboundPlanId(outbound.getOutboundPlanId());
        deleteOutboundAssignPort.updateOutboundPlanStatus(outboundPlan);
        deleteOutboundAssignPort.deleteOutboundAssign(outboundId);
    }
}

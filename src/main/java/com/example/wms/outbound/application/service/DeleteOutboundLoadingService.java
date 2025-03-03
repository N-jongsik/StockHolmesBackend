package com.example.wms.outbound.application.service;

import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.in.DeleteOutboundLoadingUseCase;
import com.example.wms.outbound.application.port.out.DeleteOutboundLoadingPort;
import com.example.wms.outbound.application.port.out.GetOutboundLoadingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteOutboundLoadingService implements DeleteOutboundLoadingUseCase {

    private final DeleteOutboundLoadingPort deleteOutboundLoadingPort;
    private final GetOutboundLoadingPort getOutboundLoadingPort;

    @Override
    public void deleteOutboundLoading(Long outboundId) {
        Outbound outbound = getOutboundLoadingPort.findOutboundByOutboundId(outboundId);
        OutboundPlan outboundPlan = getOutboundLoadingPort.findOutboundPlanByOutboundPlanId(outbound.getOutboundPlanId());
        deleteOutboundLoadingPort.updateOutboundPlanStatus(outboundPlan);
        deleteOutboundLoadingPort.deleteOutboundLoading(outboundId);
    }
}

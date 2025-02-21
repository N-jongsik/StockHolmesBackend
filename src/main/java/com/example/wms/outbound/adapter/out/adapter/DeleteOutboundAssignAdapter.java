package com.example.wms.outbound.adapter.out.adapter;

import com.example.wms.infrastructure.mapper.OutboundMapper;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.out.DeleteOutboundAssignPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteOutboundAssignAdapter implements DeleteOutboundAssignPort {

    private final OutboundMapper outboundMapper;

    @Override
    public Outbound selectOutboundAssign(Long outboundId) {
        return outboundMapper.findOutboundByOutboundId(outboundId);
    }

    @Override
    public void deleteOutboundAssign(Long outboundId) {
        outboundMapper.deleteOutboundAssign(outboundId);
    }

    @Override
    public void updateOutboundPlanStatus(OutboundPlan outboundPlan) {
        outboundMapper.updateOutboundPlanStatus(outboundPlan.getOutboundPlanId(), "출고예정");
    }
}

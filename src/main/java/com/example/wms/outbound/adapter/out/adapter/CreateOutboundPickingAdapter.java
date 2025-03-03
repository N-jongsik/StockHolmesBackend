package com.example.wms.outbound.adapter.out.adapter;

import com.example.wms.infrastructure.mapper.OutboundPickingMapper;
import com.example.wms.notification.application.domain.Notification;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.out.CreateOutboundPickingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateOutboundPickingAdapter implements CreateOutboundPickingPort {

    private final OutboundPickingMapper outboundPickingMapper;

    @Override
    public void createOutboundPicking(Outbound outbound) {
        outboundPickingMapper.insertOutboundPicking(outbound.getOutboundId(), outbound.getOutboundPickingNumber(), outbound.getOutboundPickingDate());
    }

    @Override
    public Outbound findOutboundByPlanId(Long outboundPlanId) {
        return outboundPickingMapper.findOutboundByPlanId(outboundPlanId);
    }

    @Override
    public String findMaxOutboundPickingNumber() {
        return outboundPickingMapper.findMaxOutboundPickingNumber();
    }

    @Override
    public void updateOutboundPlanStatus(OutboundPlan outboundPlan) {
        outboundPickingMapper.updateOutboundPlanStatus(outboundPlan.getOutboundPlanId(), "출고피킹");
    }
}

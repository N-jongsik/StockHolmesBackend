package com.example.wms.outbound.adapter.out.adapter;

import com.example.wms.infrastructure.mapper.OutboundPackingMapper;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.out.CreateOutboundPackingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateOutboundPackingAdapter implements CreateOutboundPackingPort {

    private final OutboundPackingMapper outboundPackingMapper;

    @Override
    public void createOutboundPacking(Outbound outbound) {
        outboundPackingMapper.insertOutboundPacking(outbound.getOutboundId(), outbound.getOutboundPackingNumber(), outbound.getOutboundPackingDate());
    }

    @Override
    public Outbound findOutboundByPlanId(Long outboundPlanId) {
        return outboundPackingMapper.findOutboundByPlanId(outboundPlanId);
    }

    @Override
    public String findMaxOutboundPackingNumber() {
        return outboundPackingMapper.findMaxOutboundPackingNumber();
    }

    @Override
    public void updateOutboundPlanStatus(OutboundPlan outboundPlan) {
        outboundPackingMapper.updateOutboundPlanStatus(outboundPlan.getOutboundPlanId(), "출고패킹");
    }
}

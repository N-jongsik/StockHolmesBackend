package com.example.wms.outbound.adapter.out.adapter;

import com.example.wms.infrastructure.mapper.OutboundLoadingMapper;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.out.CreateOutboundLoadingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateOutboundLoadingAdapter implements CreateOutboundLoadingPort {

    private final OutboundLoadingMapper outboundLoadingMapper;

    @Override
    public void createOutboundLoading(Outbound outbound) {
        outboundLoadingMapper.insertOutboundLoading(outbound.getOutboundId(), outbound.getOutboundLoadingNumber(), outbound.getOutboundLoadingDate());
    }

    @Override
    public Outbound findOutboundByPlanId(Long outboundPlanId) {
        return outboundLoadingMapper.findOutboundByPlanId(outboundPlanId);
    }

    @Override
    public String findMaxOutboundLoadingNumber() {
        return outboundLoadingMapper.findMaxOutboundLoadingNumber();
    }

    @Override
    public void updateOutboundPlanStatus(OutboundPlan outboundPlan) {
        outboundLoadingMapper.updateOutboundPlanStatus(outboundPlan.getOutboundPlanId(), "출하상차");
    }
}

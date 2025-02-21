package com.example.wms.outbound.adapter.out.adapter;

import com.example.wms.infrastructure.mapper.OutboundPlanMapper;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.out.CreateOutboundPlanPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateOutboundPlanAdapter implements CreateOutboundPlanPort {

    private final OutboundPlanMapper outboundPlanMapper;

    @Override
    public void save(OutboundPlan outboundPlan) {
        outboundPlanMapper.insert(outboundPlan);
    }
}

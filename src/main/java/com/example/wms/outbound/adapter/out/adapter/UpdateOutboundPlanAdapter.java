package com.example.wms.outbound.adapter.out.adapter;

import com.example.wms.infrastructure.mapper.OutboundPlanMapper;
import com.example.wms.outbound.adapter.in.dto.OutboundPlanRequestDto;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.out.UpdateOutboundPlanPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateOutboundPlanAdapter implements UpdateOutboundPlanPort {

    private final OutboundPlanMapper outboundPlanMapper;

    @Override
    public void updateOutboundPlan(Long outboundPlanId, OutboundPlanRequestDto outboundPlanRequestDto) {
        outboundPlanMapper.updateOutboundPlan(outboundPlanId, outboundPlanRequestDto);
    }

    @Override
    public void updateOutboundPlanProducts(Long outboundPlanId, OutboundPlanRequestDto outboundPlanRequestDto) {
        outboundPlanMapper.updateOutboundPlanProducts(outboundPlanId, outboundPlanRequestDto.getProductList());
    }
}

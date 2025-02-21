package com.example.wms.outbound.application.port.out;

import com.example.wms.outbound.adapter.in.dto.OutboundPlanRequestDto;
import com.example.wms.outbound.application.domain.OutboundPlan;

public interface UpdateOutboundPlanPort {
    void updateOutboundPlan(Long outboundPlanId, OutboundPlanRequestDto outboundPlanRequestDto);
    void updateOutboundPlanProducts(Long outboundPlanId, OutboundPlanRequestDto outboundPlanRequestDto);
}

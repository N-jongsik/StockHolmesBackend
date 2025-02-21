package com.example.wms.outbound.application.port.in;

import com.example.wms.outbound.adapter.in.dto.OutboundPlanRequestDto;

public interface UpdateOutboundPlanUseCase {
    void UpdateOutboundPlan(Long outboundId, OutboundPlanRequestDto outboundPlanRequestDto);
}

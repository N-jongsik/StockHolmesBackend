package com.example.wms.outbound.application.service;

import com.example.wms.outbound.adapter.in.dto.OutboundPlanRequestDto;
import com.example.wms.outbound.application.port.in.UpdateOutboundPlanUseCase;
import com.example.wms.outbound.application.port.out.UpdateOutboundPlanPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateOutboundPlanService implements UpdateOutboundPlanUseCase {

    private final UpdateOutboundPlanPort updateOutboundPlanPort;

    @Transactional
    @Override
    public void UpdateOutboundPlan(Long outboundId, OutboundPlanRequestDto outboundPlanRequestDto) {
        updateOutboundPlanPort.updateOutboundPlanProducts(outboundId, outboundPlanRequestDto);
        updateOutboundPlanPort.updateOutboundPlan(outboundId,outboundPlanRequestDto);
    }
}

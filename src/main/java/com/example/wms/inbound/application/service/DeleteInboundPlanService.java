package com.example.wms.inbound.application.service;

import com.example.wms.inbound.application.port.in.DeleteInboundPlanUseCase;
import com.example.wms.inbound.application.port.out.DeleteInboundPlanPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteInboundPlanService implements DeleteInboundPlanUseCase {

    private final DeleteInboundPlanPort deleteInboundPlanPort;

    @Transactional
    @Override
    public void deleteInboundPlan(Long inboundId) {
        deleteInboundPlanPort.delete(inboundId);
    }
}

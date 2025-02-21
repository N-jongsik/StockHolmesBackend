package com.example.wms.outbound.application.service;

import com.example.wms.outbound.application.port.in.UpdateOutboundAssignUseCase;
import com.example.wms.outbound.application.port.out.UpdateOutboundAssignPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UpdateOutboundAssignService implements UpdateOutboundAssignUseCase {

    private final UpdateOutboundAssignPort updateOutboundAssignPort;

    @Override
    public void updateOutboundAssign(Long outboundId, LocalDate outboundAssignDate) {
        updateOutboundAssignPort.updateOutboundAssign(outboundId, outboundAssignDate);
    }
}

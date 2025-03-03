package com.example.wms.outbound.application.service;

import com.example.wms.outbound.application.port.in.UpdateOutboundPickingUseCase;
import com.example.wms.outbound.application.port.out.UpdateOutboundPickingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UpdateOutboundPickingService implements UpdateOutboundPickingUseCase {

    private final UpdateOutboundPickingPort updateOutboundPickingPort;

    @Override
    public void updateOutboundPicking(Long outboundId, LocalDate outboundAssignDate) {
        updateOutboundPickingPort.updateOutboundPicking(outboundId, outboundAssignDate);
    }
}

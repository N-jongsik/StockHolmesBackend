package com.example.wms.outbound.application.service;

import com.example.wms.outbound.application.port.in.UpdateOutboundPackingUseCase;
import com.example.wms.outbound.application.port.out.UpdateOutboundPackingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UpdateOutboundPackingService implements UpdateOutboundPackingUseCase {

    private final UpdateOutboundPackingPort updateOutboundPackingPort;

    @Override
    public void updateOutboundPacking(Long outboundId, LocalDate outboundPackingDate) {
        updateOutboundPackingPort.updateOutboundPacking(outboundId, outboundPackingDate);
    }
}

package com.example.wms.outbound.application.service;

import com.example.wms.outbound.application.port.in.UpdateOutboundLoadingUseCase;
import com.example.wms.outbound.application.port.out.UpdateOutboundLoadingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UpdateOutboundLoadingService implements UpdateOutboundLoadingUseCase {

    private final UpdateOutboundLoadingPort updateOutboundLoadingPort;

    @Override
    public void updateOutboundLoading(Long outboundId, LocalDate outboundLoadingDate) {
        updateOutboundLoadingPort.updateOutboundLoading(outboundId, outboundLoadingDate);
    }
}

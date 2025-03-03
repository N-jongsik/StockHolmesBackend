package com.example.wms.outbound.application.port.in;

import java.time.LocalDate;

public interface UpdateOutboundPickingUseCase {
    void updateOutboundPicking(Long outboundId, LocalDate outboundPickingDate);
}

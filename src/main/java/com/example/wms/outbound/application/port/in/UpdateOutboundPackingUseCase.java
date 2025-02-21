package com.example.wms.outbound.application.port.in;

import java.time.LocalDate;

public interface UpdateOutboundPackingUseCase {
    void updateOutboundPacking(Long outboundId, LocalDate outboundPackingDate);
}

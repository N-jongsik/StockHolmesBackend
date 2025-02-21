package com.example.wms.outbound.application.port.out;

import java.time.LocalDate;

public interface UpdateOutboundPackingPort {
    void updateOutboundPacking(Long outboundId, LocalDate outboundPackingDate);
}

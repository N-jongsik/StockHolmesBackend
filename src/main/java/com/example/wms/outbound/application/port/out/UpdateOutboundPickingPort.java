package com.example.wms.outbound.application.port.out;

import java.time.LocalDate;

public interface UpdateOutboundPickingPort {
    void updateOutboundPicking(Long outboundId, LocalDate outboundPickingDate);
}

package com.example.wms.outbound.application.port.out;

import com.example.wms.outbound.application.domain.Outbound;

import java.time.LocalDate;

public interface UpdateOutboundAssignPort {
    void updateOutboundAssign(Long outboundId, LocalDate outboundAssignDate);
}

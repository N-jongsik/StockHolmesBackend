package com.example.wms.outbound.application.port.in;

import java.time.LocalDate;

public interface UpdateOutboundAssignUseCase {
    void updateOutboundAssign(Long outboundId, LocalDate outboundAssignDate);
}

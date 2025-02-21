package com.example.wms.outbound.application.port.in;

import java.time.LocalDate;

public interface UpdateOutboundLoadingUseCase {
    void updateOutboundLoading(Long outboundId, LocalDate outboundLoadingDate);
}

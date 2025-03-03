package com.example.wms.outbound.application.port.out;

import java.time.LocalDate;

public interface UpdateOutboundLoadingPort {
    void updateOutboundLoading(Long outboundId, LocalDate outboundLoadingDate);
}

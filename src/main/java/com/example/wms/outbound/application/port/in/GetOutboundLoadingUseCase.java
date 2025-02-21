package com.example.wms.outbound.application.port.in;

import com.example.wms.outbound.adapter.in.dto.OutboundLoadingResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface GetOutboundLoadingUseCase {
    Page<OutboundLoadingResponseDto> getFilteredOutboundLoadings(String outboundLoadingNumber, LocalDate startDate, LocalDate endDate, Pageable pageable);
}

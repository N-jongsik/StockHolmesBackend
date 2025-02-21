package com.example.wms.outbound.application.port.in;

import com.example.wms.outbound.adapter.in.dto.OutboundPickingResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface GetOutboundPickingUseCase {
    Page<OutboundPickingResponseDto> getFilteredOutboundPickings(String outboundPickingNumber, LocalDate startDate, LocalDate endDate, Pageable pageable);
}

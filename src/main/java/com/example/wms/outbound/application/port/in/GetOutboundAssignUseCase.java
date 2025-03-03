package com.example.wms.outbound.application.port.in;

import com.example.wms.outbound.adapter.in.dto.OutboundAssignResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface GetOutboundAssignUseCase {
    Page<OutboundAssignResponseDto> getFilteredOutboundAssings(String outboundAssignNumber, LocalDate startDate, LocalDate endDate, Pageable pageable);
}

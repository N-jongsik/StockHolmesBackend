package com.example.wms.outbound.application.port.in;

import com.example.wms.outbound.adapter.in.dto.OutboundPackingResponseDto;
import com.example.wms.outbound.adapter.in.dto.OutboundPickingResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface GetOutboundPackingUseCase {
    Page<OutboundPackingResponseDto> getFilteredOutboundPackings(String outboundPackingNumber, LocalDate startDate, LocalDate endDate, Pageable pageable);
}

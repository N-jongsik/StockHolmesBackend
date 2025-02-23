package com.example.wms.inbound.application.port.in;

import com.example.wms.inbound.adapter.in.dto.response.InboundResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface GetInboundCheckUseCase {
    Page<InboundResDto> getFilteredInboundCheck(String inboundCheckNumber, LocalDate startDate, LocalDate endDate, Pageable pageable);

}

package com.example.wms.inbound.application.port.in;

import com.example.wms.inbound.adapter.in.dto.response.InboundPutAwayResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface GetInboundPutAwayUseCase {
    Page<InboundPutAwayResDto> getFilteredPutAway(String inboundPutAwayNumber, LocalDate startDate, LocalDate endDate, Pageable pageable);

}

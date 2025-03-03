package com.example.wms.outbound.application.port.in;

import com.example.wms.outbound.adapter.in.dto.OutboundPlanResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface GetOutboundPlanUseCase {
    Page<OutboundPlanResponseDto> getFilteredOutboundPlans(String outboundScheduleNumber, LocalDate startDate, LocalDate endDate,Pageable pageable);
}

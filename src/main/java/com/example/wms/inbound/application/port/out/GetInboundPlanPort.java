package com.example.wms.inbound.application.port.out;

import com.example.wms.inbound.adapter.in.dto.response.InboundAllProductDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface GetInboundPlanPort {
    List<InboundAllProductDto> findInboundFilteringWithPagination(String inboundScheduleNumber, LocalDate startDate, LocalDate endDate, Pageable safePageable);
    Integer countFilteredInboundPlan(String inboundScheduleNumber, LocalDate startDate, LocalDate endDate);
}

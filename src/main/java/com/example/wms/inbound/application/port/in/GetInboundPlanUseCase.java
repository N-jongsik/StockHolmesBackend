package com.example.wms.inbound.application.port.in;

import com.example.wms.inbound.adapter.in.dto.response.InboundAllProductDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface GetInboundPlanUseCase {
    Page<InboundResDto> getFilteredInboundPlans(String inboundScheduleNumber, LocalDate startDate, LocalDate endDate, Pageable pageable);
}

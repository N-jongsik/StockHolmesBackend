package com.example.wms.inbound.adapter.out;

import com.example.wms.inbound.adapter.in.dto.response.InboundAllProductDto;
import com.example.wms.inbound.application.port.out.GetInboundPlanPort;
import com.example.wms.infrastructure.mapper.InboundPlanMapper;
import com.example.wms.infrastructure.mapper.InboundRetrievalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetInboundPlanAdapter implements GetInboundPlanPort {

    private final InboundPlanMapper inboundPlanMapper;
    private final InboundRetrievalMapper inboundRetrievalMapper;

    @Override
    public List<InboundAllProductDto> findInboundFilteringWithPagination(String inboundScheduleNumber, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return inboundPlanMapper.findInboundPlanFilteringWithPagination(inboundScheduleNumber, startDate, endDate, pageable);
    }

    @Override
    public Integer countFilteredInboundPlan(String inboundScheduleNumber, LocalDate startDate, LocalDate endDate) {
        return inboundPlanMapper.countAllInboundPlanFiltering(inboundScheduleNumber, startDate, endDate);
    }


}


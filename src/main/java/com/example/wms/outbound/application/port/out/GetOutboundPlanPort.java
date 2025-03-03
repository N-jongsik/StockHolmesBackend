package com.example.wms.outbound.application.port.out;

import com.example.wms.outbound.adapter.in.dto.OutboundPlanResponseDto;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GetOutboundPlanPort {
    List<OutboundPlanResponseDto> findOutboundPlanFilteringWithPageNation(String outboundScheduleNumber, LocalDate startDate, LocalDate endDate, Pageable pageable);
    Integer countFilteredOutboundPlan(String outboundScheduleNumber, LocalDate startDate, LocalDate endDate);
    List<ProductInfoDto> findProductInfoByOutboundPlanId(Long outboundPlanId);
    Optional<Outbound> findOutboundByOutboundPlanId(Long outboundPlanId);
    Optional<OutboundPlan> findOutboundPlanByOutboundPlanId(Long outboundPlanId);
}

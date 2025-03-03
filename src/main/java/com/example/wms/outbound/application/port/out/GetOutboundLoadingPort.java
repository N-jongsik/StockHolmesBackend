package com.example.wms.outbound.application.port.out;

import com.example.wms.outbound.adapter.in.dto.OutboundLoadingResponseDto;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface GetOutboundLoadingPort {
    List<OutboundLoadingResponseDto> findOutboundLoadingFilteringWithPageNation(String outboundLoadingNumber, LocalDate startDate, LocalDate endDate, Pageable pageable);
    List<ProductInfoDto> findProductInfoByOutboundPlanId(Long outboundPlanId);
    OutboundPlan findOutboundPlanByOutboundPlanId(Long outboundPlanId);
    Integer countLoading(String outboundLoadingNumber, LocalDate startDate, LocalDate endDate);
    Outbound findOutboundByOutboundId(Long outboundId);
}

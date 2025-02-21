package com.example.wms.outbound.application.port.out;

import com.example.wms.outbound.adapter.in.dto.OutboundPickingResponseDto;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface GetOutboundPickingPort {
    List<OutboundPickingResponseDto> findOutboundPickingFilteringWithPageNation(String outboundPickingNumber, LocalDate startDate, LocalDate endDate, Pageable pageable);
    List<ProductInfoDto> findProductInfoByOutboundPlanId(Long outboundPlanId);
    OutboundPlan findOutboundPlanByOutboundPlanId(Long outboundPlanId);
    Integer countAllPicking(String outboundPickingNumber, LocalDate startDate, LocalDate endDate);
    Outbound findOutboundByOutboundId(Long outboundId);
}

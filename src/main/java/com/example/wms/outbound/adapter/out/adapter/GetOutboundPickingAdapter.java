package com.example.wms.outbound.adapter.out.adapter;

import com.example.wms.infrastructure.mapper.OutboundPickingMapper;
import com.example.wms.infrastructure.mapper.OutboundPlanProductMapper;
import com.example.wms.outbound.adapter.in.dto.OutboundPickingResponseDto;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.out.GetOutboundPickingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetOutboundPickingAdapter implements GetOutboundPickingPort {

    private final OutboundPickingMapper outboundPickingMapper;
    private final OutboundPlanProductMapper outboundPlanProductMapper;

    @Override
    public List<OutboundPickingResponseDto> findOutboundPickingFilteringWithPageNation(String outboundPickingNumber, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return outboundPickingMapper.findOutboundPickingFilteringWithPageNation(outboundPickingNumber, startDate, endDate, pageable);
    }

    @Override
    public List<ProductInfoDto> findProductInfoByOutboundPlanId(Long outboundPlanId) {
        return outboundPlanProductMapper.findProductInfoByOutboundPlanId(outboundPlanId);
    }

    @Override
    public OutboundPlan findOutboundPlanByOutboundPlanId(Long outboundPlanId) {
        return outboundPickingMapper.findOutboundPlanByOutboundPlanId(outboundPlanId);
    }

    @Override
    public Integer countAllPicking(String outboundPickingNumber, LocalDate startDate, LocalDate endDate) {
        return outboundPickingMapper.countAllPicking(outboundPickingNumber, startDate, endDate);
    }

    @Override
    public Outbound findOutboundByOutboundId(Long outboundId) {
        return outboundPickingMapper.findOutboundByOutboundId(outboundId);
    }
}

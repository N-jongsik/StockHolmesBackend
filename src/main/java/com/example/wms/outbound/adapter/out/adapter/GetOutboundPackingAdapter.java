package com.example.wms.outbound.adapter.out.adapter;

import com.example.wms.infrastructure.mapper.OutboundPackingMapper;
import com.example.wms.infrastructure.mapper.OutboundPlanProductMapper;
import com.example.wms.outbound.adapter.in.dto.OutboundPackingResponseDto;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.out.GetOutboundPackingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetOutboundPackingAdapter implements GetOutboundPackingPort {

    private final OutboundPackingMapper outboundPackingMapper;
    private final OutboundPlanProductMapper outboundPlanProductMapper;

    @Override
    public List<OutboundPackingResponseDto> findOutboundPackingFilteringWithPageNation(String outboundPackingNumber, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return outboundPackingMapper.findOutboundPackingFilteringWithPageNation(outboundPackingNumber, startDate, endDate,pageable);
    }

    @Override
    public List<ProductInfoDto> findProductInfoByOutboundPlanId(Long outboundPlanId) {
        return outboundPlanProductMapper.findProductInfoByOutboundPlanId(outboundPlanId);
    }

    @Override
    public OutboundPlan findOutboundPlanByOutboundPlanId(Long outboundPlanId) {
        return outboundPackingMapper.findOutboundPlanByOutboundPlanId(outboundPlanId);
    }

    @Override
    public Integer countPacking(String outboundPackingNumber, LocalDate startDate, LocalDate endDate) {
        return outboundPackingMapper.countPacking(outboundPackingNumber, startDate, endDate);
    }

    @Override
    public Outbound findOutboundByOutboundId(Long outboundId) {
        return outboundPackingMapper.findOutboundByOutboundId(outboundId);
    }
}

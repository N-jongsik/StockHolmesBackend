package com.example.wms.outbound.adapter.out.adapter;

import com.example.wms.infrastructure.mapper.OutboundLoadingMapper;
import com.example.wms.infrastructure.mapper.OutboundPlanProductMapper;
import com.example.wms.outbound.adapter.in.dto.OutboundLoadingResponseDto;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.out.GetOutboundLoadingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetOutboundLoadingAdapter implements GetOutboundLoadingPort {

    private final OutboundLoadingMapper outboundLoadingMapper;
    private final OutboundPlanProductMapper outboundPlanProductMapper;

    @Override
    public List<OutboundLoadingResponseDto> findOutboundLoadingFilteringWithPageNation(String outboundLoadingNumber, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return outboundLoadingMapper.findOutboundLoadingFilteringWithPageNation(outboundLoadingNumber, startDate, endDate, pageable);
    }

    @Override
    public List<ProductInfoDto> findProductInfoByOutboundPlanId(Long outboundPlanId) {
        return outboundPlanProductMapper.findProductInfoByOutboundPlanId(outboundPlanId);
    }

    @Override
    public OutboundPlan findOutboundPlanByOutboundPlanId(Long outboundPlanId) {
        return outboundLoadingMapper.findOutboundPlanByOutboundPlanId(outboundPlanId);
    }

    @Override
    public Integer countLoading(String outboundLoadingNumber, LocalDate startDate, LocalDate endDate) {
        return outboundLoadingMapper.countLoading(outboundLoadingNumber, startDate, endDate);
    }

    @Override
    public Outbound findOutboundByOutboundId(Long outboundId) {
        return outboundLoadingMapper.findOutboundByOutboundId(outboundId);
    }
}

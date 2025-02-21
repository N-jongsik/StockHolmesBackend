package com.example.wms.outbound.adapter.out.adapter;

import com.example.wms.infrastructure.mapper.OutboundAssignMapper;
import com.example.wms.infrastructure.mapper.OutboundMapper;
import com.example.wms.infrastructure.mapper.OutboundPlanProductMapper;
import com.example.wms.outbound.adapter.in.dto.OutboundAssignResponseDto;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.out.GetOutboundAssignPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetOutboundAssignAdapter implements GetOutboundAssignPort {

    private final OutboundAssignMapper outboundAssignMapper;
    private final OutboundPlanProductMapper outboundPlanProductMapper;

    @Override
    public List<OutboundAssignResponseDto> findOutboundAssignFilteringWithPageNation(String outboundAssignNumber, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return outboundAssignMapper.findOutboundAssignFilteringWithPageNation(outboundAssignNumber, startDate, endDate, pageable);
    }

    @Override
    public List<ProductInfoDto> findProductInfoByOutboundPlanId(Long outboundPlanId) {
        return outboundPlanProductMapper.findProductInfoByOutboundPlanId(outboundPlanId);
    }

    @Override
    public OutboundPlan findOutboundPlanByOutboundPlanId(Long outboundPlanId) {
        return outboundAssignMapper.findOutboundPlanByOutboundPlanId(outboundPlanId);
    }

    @Override
    public Integer countAssign(String outboundAssignNumber, LocalDate startDate, LocalDate endDate) {
        return outboundAssignMapper.countAssign(outboundAssignNumber, startDate, endDate);
    }

    @Override
    public Outbound findOutboundByOutboundId(Long outboundId) {
        return outboundAssignMapper.findOutboundByOutboundId(outboundId);
    }
}

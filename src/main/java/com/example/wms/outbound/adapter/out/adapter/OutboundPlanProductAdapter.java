package com.example.wms.outbound.adapter.out.adapter;

import com.example.wms.infrastructure.mapper.OutboundPlanMapper;
import com.example.wms.infrastructure.mapper.OutboundPlanProductMapper;
import com.example.wms.outbound.adapter.in.dto.ABCAnalysisDataDto;
import com.example.wms.outbound.application.port.out.OutboundPlanProductPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboundPlanProductAdapter implements OutboundPlanProductPort {

    private final OutboundPlanProductMapper outboundPlanProductMapper;

    @Override
    public List<ABCAnalysisDataDto> getRequiredQuantitiesPerProduct() {
        return outboundPlanProductMapper.getRequiredQuantitiesPerProduct();
    }
}

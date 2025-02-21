package com.example.wms.outbound.adapter.out.adapter;

import com.example.wms.infrastructure.mapper.OutboundPlanProductMapper;
import com.example.wms.outbound.application.domain.OutboundPlanProduct;
import com.example.wms.outbound.application.port.out.CreateOutboundPlanProductPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateOutboundPlanProductAdapter implements CreateOutboundPlanProductPort {

    private final OutboundPlanProductMapper outboundPlanProductMapper;

    @Override
    public void saveAll(List<OutboundPlanProduct> outboundPlanProductList) {
        outboundPlanProductMapper.batchInsert(outboundPlanProductList);
    }
}

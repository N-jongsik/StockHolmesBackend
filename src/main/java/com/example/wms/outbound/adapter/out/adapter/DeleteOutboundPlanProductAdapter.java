package com.example.wms.outbound.adapter.out.adapter;

import com.example.wms.infrastructure.mapper.OutboundPlanMapper;
import com.example.wms.outbound.application.port.out.DeleteOutboundPlanProductPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteOutboundPlanProductAdapter implements DeleteOutboundPlanProductPort {

    private final OutboundPlanMapper outboundPlanMapper;

    @Override
    public void deleteOutboundPlanProduct(Long outboundPlanId) {
        outboundPlanMapper.deleteOutboundPlanAndProducts(outboundPlanId);
    }
}

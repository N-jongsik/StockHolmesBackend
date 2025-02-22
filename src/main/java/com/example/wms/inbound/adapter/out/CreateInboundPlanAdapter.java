package com.example.wms.inbound.adapter.out;

import com.example.wms.inbound.application.domain.Inbound;
import com.example.wms.inbound.application.port.out.CreateInboundPlanPort;
import com.example.wms.infrastructure.mapper.InboundMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateInboundPlanAdapter implements CreateInboundPlanPort {

    private final InboundMapper inboundMapper;

    @Override
    public void save(Inbound inbound) {
        inboundMapper.insert(inbound);
    }
}

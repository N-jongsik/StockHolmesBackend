package com.example.wms.inbound.adapter.out;

import com.example.wms.inbound.application.port.out.DeleteInboundPlanPort;
import com.example.wms.infrastructure.mapper.InboundMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class DeleteInboundPlanAdapter implements DeleteInboundPlanPort {

    private final InboundMapper inboundMapper;

    @Override
    public void delete(Long inboundId) {
        inboundMapper.delete(inboundId);
    }

}

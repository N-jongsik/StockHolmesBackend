package com.example.wms.inbound.adapter.out;

import com.example.wms.inbound.application.port.out.AssignInboundNumberPort;
import com.example.wms.infrastructure.mapper.InboundNumberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssignInboundNumberAdapter implements AssignInboundNumberPort {

    private final InboundNumberMapper inboundNumberMapper;

    @Override
    public String findMaxISNumber() {
        return inboundNumberMapper.findMaxISNumber();
    }

    @Override
    public String findMaxICNumber() {
        return inboundNumberMapper.findMaxICNumber();
    }

    @Override
    public String findMaxPANumber() {
        return inboundNumberMapper.findMaxPANumber();
    }
}

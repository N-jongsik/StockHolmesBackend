package com.example.wms.inbound.adapter.out;

import com.example.wms.inbound.application.port.out.AssignInboundNumberPort;
import com.example.wms.infrastructure.mapper.InboundNumberMapper;
import com.example.wms.infrastructure.mapper.LotMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssignInboundNumberAdapter implements AssignInboundNumberPort {

    private final InboundNumberMapper inboundNumberMapper;
    private final LotMapper lotMapper;

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

    @Override
    public String findMaxLONumber() {
        return lotMapper.findMaxLONumber();
    }

}

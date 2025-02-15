package com.example.wms.inbound.adapter.out;

import com.example.wms.inbound.application.domain.InboundCheck;
import com.example.wms.inbound.application.port.out.InboundCheckPort;
import com.example.wms.infrastructure.mapper.InboundCheckMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InboundCheckAdapter implements InboundCheckPort {

    private final InboundCheckMapper inboundCheckMapper;

    @Override
    public void save(InboundCheck inboundCheck) {
        inboundCheckMapper.insertInboundCheck(inboundCheck);
    }

    @Override
    public void findByInboundId(Long inboundId) {
        inboundCheckMapper.findByInboundId(inboundId);
    }
}

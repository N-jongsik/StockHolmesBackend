package com.example.wms.inbound.adapter.out;

import com.example.wms.inbound.application.port.out.UpdateInboundCheckPort;
import com.example.wms.infrastructure.mapper.InboundMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UpdateInboundCheckAdapter implements UpdateInboundCheckPort {

    private final InboundMapper inboundMapper;

    @Override
    public void updateInboundCheck(String scheduleNumber, String checkNumber) {
        inboundMapper.updateInboundWorkerCheck(scheduleNumber, checkNumber);
    }

}

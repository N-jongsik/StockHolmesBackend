package com.example.wms.outbound.adapter.out.adapter;

import com.example.wms.infrastructure.mapper.CalculateOsNumberMapper;
import com.example.wms.outbound.application.port.out.CalculateOsNumberPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CalculateOsNumberAdapter implements CalculateOsNumberPort {

    private final CalculateOsNumberMapper calculateOsNumberMapper;

    @Override
    public String findMaxOutboundScheduleNumber() {
        return calculateOsNumberMapper.findMaxOutboundScheduleNumber();
    }
}

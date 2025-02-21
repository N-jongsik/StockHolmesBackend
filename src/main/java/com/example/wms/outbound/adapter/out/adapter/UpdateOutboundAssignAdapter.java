package com.example.wms.outbound.adapter.out.adapter;

import com.example.wms.infrastructure.mapper.OutboundMapper;
import com.example.wms.outbound.application.port.out.UpdateOutboundAssignPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class UpdateOutboundAssignAdapter implements UpdateOutboundAssignPort {

    private final OutboundMapper outboundMapper;

    @Override
    public void updateOutboundAssign(Long outboundId, LocalDate outboundAssignDate) {
        outboundMapper.updateOutboundAssign(outboundId, outboundAssignDate);
    }
}

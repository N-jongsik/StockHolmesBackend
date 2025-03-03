package com.example.wms.outbound.adapter.out.adapter;

import com.example.wms.infrastructure.mapper.OutboundPickingMapper;
import com.example.wms.outbound.application.port.out.UpdateOutboundPickingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class UpdateOutboundPickingAdapter implements UpdateOutboundPickingPort {

    private final OutboundPickingMapper outboundPickingMapper;

    @Override
    public void updateOutboundPicking(Long outboundId, LocalDate outboundPickingDate) {
        outboundPickingMapper.updateOutboundPicking(outboundId, outboundPickingDate);
    }
}

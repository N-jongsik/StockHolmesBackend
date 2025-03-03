package com.example.wms.outbound.adapter.out.adapter;

import com.example.wms.infrastructure.mapper.OutboundPackingMapper;
import com.example.wms.outbound.application.port.out.UpdateOutboundPackingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class UpdateOutboundPackingAdapter implements UpdateOutboundPackingPort {

    private final OutboundPackingMapper outboundPackingMapper;

    @Override
    public void updateOutboundPacking(Long outboundId, LocalDate outboundPackingDate) {
        outboundPackingMapper.updateOutboundPacking(outboundId, outboundPackingDate);
    }
}

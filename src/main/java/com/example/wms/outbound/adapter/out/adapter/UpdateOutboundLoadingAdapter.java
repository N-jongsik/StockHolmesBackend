package com.example.wms.outbound.adapter.out.adapter;

import com.example.wms.infrastructure.mapper.OutboundLoadingMapper;
import com.example.wms.outbound.application.port.out.UpdateOutboundLoadingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class UpdateOutboundLoadingAdapter implements UpdateOutboundLoadingPort {

    private final OutboundLoadingMapper outboundLoadingMapper;

    @Override
    public void updateOutboundLoading(Long outboundId, LocalDate outboundLoadingDate) {
        outboundLoadingMapper.updateOutboundLoading(outboundId, outboundLoadingDate);
    }
}

package com.example.wms.dashboard.adapter.out;

import com.example.wms.dashboard.adapter.in.dto.InboundStatusResponseDto;
import com.example.wms.dashboard.adapter.in.dto.OrderStatusResponseDto;
import com.example.wms.dashboard.adapter.in.dto.OutboundStatusResponseDto;
import com.example.wms.dashboard.application.port.out.DashboardPort;
import com.example.wms.infrastructure.mapper.DashboardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DashboardAdapter implements DashboardPort {
    private final DashboardMapper dashboardMapper;

    public OutboundStatusResponseDto selectOutboundStatusCounts() {
        return dashboardMapper.selectOutboundStatusCounts();
    }

    @Override
    public InboundStatusResponseDto selectInboundStatusCounts() {
        return dashboardMapper.selectInboundStatusCounts();
    }

    @Override
    public OrderStatusResponseDto selectOrderStatusCounts() {
        return dashboardMapper.selectOrderStatusCounts();
    }
}

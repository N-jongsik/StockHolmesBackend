package com.example.wms.dashboard.application.service;

import com.example.wms.dashboard.adapter.in.dto.OutboundStatusResponseDto;
import com.example.wms.dashboard.application.port.in.DashboardUseCase;
import com.example.wms.dashboard.application.port.out.DashboardPort;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DashboardService implements DashboardUseCase {
    private final DashboardPort dashboardPort;

    @Override
    public OutboundStatusResponseDto getOutboundStatus() {
        return dashboardPort.selectOutboundStatusCounts();
    }
}

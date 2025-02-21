package com.example.wms.dashboard.application.port.out;

import com.example.wms.dashboard.adapter.in.dto.InboundStatusResponseDto;
import com.example.wms.dashboard.adapter.in.dto.OrderStatusResponseDto;
import com.example.wms.dashboard.adapter.in.dto.OutboundStatusResponseDto;

public interface DashboardPort {
    OutboundStatusResponseDto selectOutboundStatusCounts();
    InboundStatusResponseDto selectInboundStatusCounts();
    OrderStatusResponseDto selectOrderStatusCounts();
}

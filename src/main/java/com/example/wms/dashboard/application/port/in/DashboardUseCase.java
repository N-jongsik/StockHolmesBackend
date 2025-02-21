package com.example.wms.dashboard.application.port.in;

import com.example.wms.dashboard.adapter.in.dto.InboundStatusResponseDto;
import com.example.wms.dashboard.adapter.in.dto.OrderStatusResponseDto;
import com.example.wms.dashboard.adapter.in.dto.OutboundStatusResponseDto;
import com.example.wms.dashboard.adapter.in.dto.TodayCountDto;

public interface DashboardUseCase {
    OutboundStatusResponseDto getOutboundStatus();
    InboundStatusResponseDto getInboundStatus();
    OrderStatusResponseDto getOrderStatus();
    TodayCountDto getTodayReceived();
    TodayCountDto getTodayCompleted();
}

package com.example.wms.dashboard.application.port.out;

import com.example.wms.dashboard.adapter.in.dto.InboundStatusResponseDto;
import com.example.wms.dashboard.adapter.in.dto.OrderStatusResponseDto;
import com.example.wms.dashboard.adapter.in.dto.OutboundStatusResponseDto;
import com.example.wms.dashboard.adapter.in.dto.TodayCountDto;

public interface DashboardPort {
    OutboundStatusResponseDto selectOutboundStatusCounts();
    InboundStatusResponseDto selectInboundStatusCounts();
    OrderStatusResponseDto selectOrderStatusCounts();
    TodayCountDto selectTodayReceivedInboundCounts();
    TodayCountDto selectTodayReceivedOutboundCounts();
    TodayCountDto selectTodayCompletedInboundCounts();
    TodayCountDto selectTodayCompletedOutboundCounts();
}

package com.example.wms.dashboard.adapter.in;

import com.example.wms.dashboard.adapter.in.dto.InboundStatusResponseDto;
import com.example.wms.dashboard.adapter.in.dto.OrderStatusResponseDto;
import com.example.wms.dashboard.adapter.in.dto.OutboundStatusResponseDto;
import com.example.wms.dashboard.adapter.in.dto.TodayCountDto;
import com.example.wms.dashboard.application.port.in.DashboardUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
@Tag(name = "Dashboard", description = "Dashboard 관련 API")
public class DashboardController {
    private final DashboardUseCase dashboardUseCase;

    @GetMapping("/outbound-status")
    @Operation(
            summary = "출고현황 조회",
            description = "출고 상태별 개수를 조회할 수 있습니다.\n"
    )
    public ResponseEntity<OutboundStatusResponseDto> getOutboundStatus() {
        return ResponseEntity.ok(dashboardUseCase.getOutboundStatus());
    }

    @GetMapping("/inbound-status")
    @Operation(
            summary = "입고현황 조회",
            description = "입고 상태별 개수를 조회할 수 있습니다.\n"
    )
    public ResponseEntity<InboundStatusResponseDto> getInboundStatus() {
        return ResponseEntity.ok(dashboardUseCase.getInboundStatus());
    }

    @GetMapping("/order-status")
    @Operation(
            summary = "발주현황 조회",
            description = "승인/미승인 발주 개수를 조회할 수 있습니다.\n"
    )
    public ResponseEntity<OrderStatusResponseDto> getOrderStatus() {
        return ResponseEntity.ok(dashboardUseCase.getOrderStatus());
    }

    @GetMapping("/today-received-inbound")
    @Operation(
            summary = "오늘 들어올 입고 개수 조회",
            description = "오늘 들어올 입고 개수를 조회할 수 있습니다.\n"
    )
    public ResponseEntity<TodayCountDto> getTodayReceivedInbound() {
        return ResponseEntity.ok(dashboardUseCase.getTodayReceivedInbound());
    }
    @GetMapping("/today-received-outbound")
    @Operation(
            summary = "오늘 들어올 출고 개수 조회",
            description = "오늘 들어올 출고 개수를 조회할 수 있습니다.\n"
    )
    public ResponseEntity<TodayCountDto> getTodayReceivedOutbound() {
        return ResponseEntity.ok(dashboardUseCase.getTodayReceivedOutbound());
    }

    @GetMapping("/today-completed-inbound")
    @Operation(
            summary = "오늘 완료된 입고 개수 조회",
            description = "오늘 완료된 입고 개수를 조회할 수 있습니다.\n"
    )
    public ResponseEntity<TodayCountDto> getTodayCompletedInbound() {
        return ResponseEntity.ok(dashboardUseCase.getTodayCompletedInbound());
    }
    @GetMapping("/today-completed-outbound")
    @Operation(
            summary = "오늘 완료된 출고 개수 조회",
            description = "오늘 완료된 출고 개수를 조회할 수 있습니다.\n"
    )
    public ResponseEntity<TodayCountDto> getTodayCompletedOutbound() {
        return ResponseEntity.ok(dashboardUseCase.getTodayCompletedOutbound());
    }
}

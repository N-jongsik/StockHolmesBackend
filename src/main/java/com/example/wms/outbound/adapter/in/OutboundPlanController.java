package com.example.wms.outbound.adapter.in;

import com.example.wms.notification.application.domain.Notification;
import com.example.wms.notification.application.port.in.NotificationUseCase;
import com.example.wms.outbound.adapter.in.dto.OutboundPlanRequestDto;
import com.example.wms.outbound.adapter.in.dto.OutboundPlanResponseDto;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.in.*;
import com.example.wms.user.application.domain.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;


@RestController
@RequiredArgsConstructor
@RequestMapping("/outbound")
@Tag(name = "출고 예정 관련 API")
public class OutboundPlanController {

    private final CreateOutboundPlanUseCase createOutboundPlanUseCase;
    private final CreateOutboundPlanProductUseCase createOutboundPlanProductUseCase;
    private final DeleteOutboundPlanProductUseCase deleteOutboundPlanProductUseCase;
    private final GetOutboundPlanUseCase getOutboundPlanUseCase;
    private final UpdateOutboundPlanUseCase updateOutboundPlanUseCase;
    private final NotificationUseCase notificationUseCase;

    @PostMapping("/register")
    @Operation(summary = "출고 예정 생성하기", description = "outboundPlan & outboundPlanProduct 생성됨")
    public ResponseEntity<Void> createOutbound(@RequestBody OutboundPlanRequestDto outboundPlanRequestDto) {
        Long outboundPlanId = createOutboundPlanUseCase.createOutbound(outboundPlanRequestDto);
        System.out.println("😶‍🌫️ 출고출고출고"+outboundPlanId);
        Notification notification = createOutboundPlanProductUseCase.createOutboundPlanProduct(outboundPlanId,outboundPlanRequestDto.getProductList());
        // UserRole 가져오기
        notificationUseCase.send(UserRole.ROLE_ADMIN, notification);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{outboundPlanId}")
    @Operation(summary = "출고 예정 삭제하기", description = "outboundPlan & outboundPlanProduct 삭제됨")
    public ResponseEntity<Void> deleteOutbound(@PathVariable Long outboundPlanId) {
        deleteOutboundPlanProductUseCase.deleteOutboundPlanProduct(outboundPlanId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "출고 예정 조회하기" , description = "필터링 값 없으면 전체조회")
    public ResponseEntity<?> getOutboundPlans(
            @RequestParam(value = "number", required = false) String outboundScheduleNumber,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(getOutboundPlanUseCase.getFilteredOutboundPlans(outboundScheduleNumber, startDate, endDate, pageable));
    }

    @PutMapping("/{outboundPlanId}")
    @Operation(summary = "출고 예정 수정하기")
    public ResponseEntity<?> updateOutboundPlan(@PathVariable Long outboundPlanId, @RequestBody OutboundPlanRequestDto outboundPlanRequestDto) {
        updateOutboundPlanUseCase.UpdateOutboundPlan(outboundPlanId, outboundPlanRequestDto);
        return ResponseEntity.ok().build();
    }
}

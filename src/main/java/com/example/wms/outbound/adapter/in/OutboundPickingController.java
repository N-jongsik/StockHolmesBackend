package com.example.wms.outbound.adapter.in;

import com.example.wms.notification.application.domain.Notification;
import com.example.wms.notification.application.port.in.NotificationUseCase;
import com.example.wms.outbound.application.port.in.CreateOutboundPickingUseCase;
import com.example.wms.outbound.application.port.in.DeleteOutboundPickingUseCase;
import com.example.wms.outbound.application.port.in.GetOutboundPickingUseCase;
import com.example.wms.outbound.application.port.in.UpdateOutboundPickingUseCase;
import com.example.wms.user.application.domain.enums.UserRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/outboundPicking")
@Tag(name = "출고 피킹 관련 API")
public class OutboundPickingController {

    private final CreateOutboundPickingUseCase createOutboundPickingUseCase;
    private final DeleteOutboundPickingUseCase deleteOutboundPickingUseCase;
    private final UpdateOutboundPickingUseCase updateOutboundPickingUseCase;
    private final GetOutboundPickingUseCase getOutboundPickingUseCase;
    private final NotificationUseCase notificationUseCase;

    @PutMapping("register/{outboundPlanId}")
    @Operation(summary = "출고 피킹 등록", description = "outboundPickingDate & Number 생성")
    public ResponseEntity<Void> createOutboundPicking(@PathVariable Long outboundPlanId) {
        Notification notification = createOutboundPickingUseCase.createOutboundPicking(outboundPlanId);
        notificationUseCase.send(UserRole.ROLE_ADMIN,notification);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{outboundId}")
    @Operation(summary = "출고 피킹 수정 & 삭제")
    public ResponseEntity<Void> deleteOutboundPicking(
            @PathVariable Long outboundId,
            @RequestBody(required = false) Map<String,LocalDate> outboundPickingDate) {
        if (!outboundPickingDate.isEmpty()) {
            LocalDate date = outboundPickingDate.get("date");
            updateOutboundPickingUseCase.updateOutboundPicking(outboundId, date);
        } else {
            deleteOutboundPickingUseCase.deleteOutboundPicking(outboundId);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "출고 피킹 조회하기", description = "필터링 값 없으면 전체조회")
    public ResponseEntity<?> getOutboundPicking(
            @RequestParam(value = "number", required = false) String outboundPickingNumber,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(getOutboundPickingUseCase.getFilteredOutboundPickings(outboundPickingNumber, startDate, endDate, pageable));
    }
}

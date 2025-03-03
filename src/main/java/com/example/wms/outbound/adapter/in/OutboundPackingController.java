package com.example.wms.outbound.adapter.in;

import com.example.wms.notification.application.domain.Notification;
import com.example.wms.notification.application.port.in.NotificationUseCase;
import com.example.wms.outbound.application.port.in.CreateOutboundPackingUseCase;
import com.example.wms.outbound.application.port.in.DeleteOutboundPackingUseCase;
import com.example.wms.outbound.application.port.in.GetOutboundPackingUseCase;
import com.example.wms.outbound.application.port.in.UpdateOutboundPackingUseCase;
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
@RequestMapping("/outboundPacking")
@Tag(name = "출고 패킹 관련 API")
public class OutboundPackingController {
    private final CreateOutboundPackingUseCase createOutboundPackingUseCase;
    private final NotificationUseCase notificationUseCase;
    private final DeleteOutboundPackingUseCase deleteOutboundPackingUseCase;
    private final UpdateOutboundPackingUseCase updateOutboundPackingUseCase;
    private final GetOutboundPackingUseCase getOutboundPackingUseCase;

    @PutMapping("/register/{outboundPlanId}")
    @Operation(summary = "출고 패킹 등록")
    public ResponseEntity<Void> createOutboundPacking(@PathVariable("outboundPlanId") Long outboundPlanId) {
        Notification notification = createOutboundPackingUseCase.createOutboundPacking(outboundPlanId);
        notificationUseCase.send(UserRole.ROLE_ADMIN,notification);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{outboundId}")
    @Operation(summary = "출고 패킹 수정 & 삭제")
    public ResponseEntity<Void> deleteOutboundPacking(
            @PathVariable Long outboundId,
            @RequestBody(required = false) Map<String,LocalDate> outboundPackingDate) {
        if (!outboundPackingDate.isEmpty()) {
            LocalDate date = outboundPackingDate.get("date");
            updateOutboundPackingUseCase.updateOutboundPacking(outboundId, date);
        } else {
            deleteOutboundPackingUseCase.deleteOutboundPacking(outboundId);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "출고 패킹 조회")
    public ResponseEntity<?> getOutboundPacking(
            @RequestParam(value = "number", required = false) String outboundPackingNumber,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @ParameterObject Pageable pageable
    ){
        return ResponseEntity.ok(getOutboundPackingUseCase.getFilteredOutboundPackings(outboundPackingNumber, startDate, endDate, pageable));
    }

}

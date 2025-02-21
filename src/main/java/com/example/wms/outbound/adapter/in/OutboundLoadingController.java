package com.example.wms.outbound.adapter.in;

import com.example.wms.notification.application.domain.Notification;
import com.example.wms.notification.application.port.in.NotificationUseCase;
import com.example.wms.outbound.application.port.in.CreateOutboundLoadingUseCase;
import com.example.wms.outbound.application.port.in.DeleteOutboundLoadingUseCase;
import com.example.wms.outbound.application.port.in.GetOutboundLoadingUseCase;
import com.example.wms.outbound.application.port.in.UpdateOutboundLoadingUseCase;
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
@RequestMapping("/outboundLoading")
@Tag(name = "출고 로딩 관련 API")
public class OutboundLoadingController {
    private final CreateOutboundLoadingUseCase createOutboundLoadingUseCase;
    private final DeleteOutboundLoadingUseCase deleteOutboundLoadingUseCase;
    private final UpdateOutboundLoadingUseCase updateOutboundLoadingUseCase;
    private final GetOutboundLoadingUseCase getOutboundLoadingUseCase;
    private final NotificationUseCase notificationUseCase;

    @PutMapping("/register/{outboundPlanId}")
    @Operation(summary = "출고 로딩 등록")
    public ResponseEntity<Void> createOutboundPacking(@PathVariable Long outboundPlanId) {
        Notification notification = createOutboundLoadingUseCase.createOutboundLoading(outboundPlanId);
        notificationUseCase.send(UserRole.ROLE_ADMIN, notification);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{outboundId}")
    @Operation(summary = "출고 로딩 수정 & 삭제")
    public ResponseEntity<Void> deleteOutboundLoading(
            @PathVariable Long outboundId,
            @RequestBody(required = false) Map<String,LocalDate> outboundLoadingDate){
        if (!outboundLoadingDate.isEmpty()) {
            LocalDate date = outboundLoadingDate.get("date");
            updateOutboundLoadingUseCase.updateOutboundLoading(outboundId, date);
        } else {
            deleteOutboundLoadingUseCase.deleteOutboundLoading(outboundId);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "출고 로딩 조회")
    public ResponseEntity<?> getOutboundLoading(
            @RequestParam(value = "number", required = false) String outboundLoadingNumber,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @ParameterObject Pageable pageable
    ){
        return ResponseEntity.ok(getOutboundLoadingUseCase.getFilteredOutboundLoadings(outboundLoadingNumber, startDate, endDate, pageable));
    }
}

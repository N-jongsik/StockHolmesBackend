package com.example.wms.notification.adapter.in;

import com.example.wms.notification.adapter.SseEmitters;
import com.example.wms.notification.application.port.in.NotificationUseCase;
import com.example.wms.user.application.domain.enums.UserRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Tag(name = "알림 관련 API")
public class NotificationController {

    private final NotificationUseCase notificationUseCase;

    @Operation(summary = "클라이언트와 연결 맺기")
    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(@RequestParam UserRole role) {
        // User Role가져오기
        SseEmitter emitter = notificationUseCase.connect(role);
        return ResponseEntity.ok(emitter);
    }

}

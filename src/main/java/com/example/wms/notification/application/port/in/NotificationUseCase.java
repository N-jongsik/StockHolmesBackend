package com.example.wms.notification.application.port.in;

import com.example.wms.notification.application.domain.Notification;
import com.example.wms.user.application.domain.enums.UserRole;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationUseCase {
    SseEmitter connect(UserRole userRole);
    void send(UserRole userRole, Notification notification);
}

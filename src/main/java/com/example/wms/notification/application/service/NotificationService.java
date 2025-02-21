package com.example.wms.notification.application.service;

import com.example.wms.notification.adapter.SseEmitters;
import com.example.wms.notification.application.domain.Notification;
import com.example.wms.notification.application.port.in.NotificationUseCase;
import com.example.wms.user.application.domain.enums.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService implements NotificationUseCase {

    private final SseEmitters sseEmitters;
    private final Map<UserRole, List<SseEmitter>> emitters = new ConcurrentHashMap<>();
    private final Map<UserRole, List<Notification>> notificationQueue = new ConcurrentHashMap<>();

    @Override
    public SseEmitter connect(UserRole userRole) {
        SseEmitter emitter = new SseEmitter(10 * 60 * 1000L);

        emitters.computeIfAbsent(userRole, k -> new ArrayList<>()).add(emitter);

        log.info("✅ SSE 연결됨: {} (현재 등록된 emitter 수: {})", userRole, emitters.get(userRole).size());

        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("connected!"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        emitter.onCompletion(() -> removeEmitter(userRole, emitter));
        emitter.onTimeout(() -> removeEmitter(userRole, emitter));

        sendQueuedNotifications(userRole, emitter);

        return emitter;
    }

    private void removeEmitter(UserRole userRole, SseEmitter emitter) {
        emitters.computeIfPresent(userRole, (key, list) -> {
            list.remove(emitter);
            log.info("🛑 SSE 연결 해제됨: {} (남은 emitter 수: {})", userRole, list.size());
            return list.isEmpty() ? new ArrayList<>() : list;
        });
    }

    @Override
    public void send(UserRole userRole, Notification notification) {
        List<SseEmitter> roleEmitters = emitters.getOrDefault(userRole, new ArrayList<>());

        log.info("📢 알림 전송 요청: {} (현재 등록된 emitter 수: {})", userRole, roleEmitters.size());

        if (roleEmitters.isEmpty()) {
            log.warn("🚨 No active emitters found for role: " + userRole);
            notificationQueue.computeIfAbsent(userRole, k -> new ArrayList<>()).add(notification);
            return;
        }

        for (Iterator<SseEmitter> iterator = roleEmitters.iterator(); iterator.hasNext(); ) {
            SseEmitter emitter = iterator.next();
            try {
                emitter.send(SseEmitter.event().name("NOTIFICATION").data(notification));
                log.info("📩 알림 전송 완료 to {}: {}", userRole, notification.getContent());
            } catch (IOException e) {
                iterator.remove();
                log.info("❌ 알림 전송 실패, emitter 제거: {}", userRole);
            }
        }
    }

    // emitter연결이 끊기면 큐에 알림 내용 쌓임
    private void sendQueuedNotifications(UserRole userRole, SseEmitter emitter) {
        List<Notification> queuedNotifications = notificationQueue.getOrDefault(userRole, new ArrayList<>());

        for (Notification notification : queuedNotifications) {
            try {
                emitter.send(SseEmitter.event().name("NOTIFICATION").data(notification));
                log.info("📨 큐에서 알림 전송 to {}: {}", userRole, notification.getContent());
            } catch (IOException e) {
                log.error("❌ 큐 알림 전송 실패 to {}: {}", userRole, e.getMessage());
            }
        }

        notificationQueue.remove(userRole);
    }
}

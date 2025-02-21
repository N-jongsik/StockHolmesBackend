package com.example.wms.notification.adapter.out;

import com.example.wms.infrastructure.mapper.NotificationMapper;
import com.example.wms.notification.application.domain.Notification;
import com.example.wms.notification.application.port.out.NotificationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationAdapter implements NotificationPort {

    private final NotificationMapper notificationMapper;

    @Override
    public void save(Notification notification) {
        notificationMapper.insert(notification);
    }

    @Override
    public void get(Notification notification) {
        notificationMapper.getAll(notification.getUserRole());
    }
}

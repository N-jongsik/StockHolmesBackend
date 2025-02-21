package com.example.wms.notification.application.port.out;

import com.example.wms.notification.application.domain.Notification;

public interface NotificationPort {
    void save(Notification notification);
    void get(Notification notification);
}

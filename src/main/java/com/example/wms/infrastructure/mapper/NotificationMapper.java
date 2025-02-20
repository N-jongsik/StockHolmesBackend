package com.example.wms.infrastructure.mapper;

import com.example.wms.notification.application.domain.Notification;
import com.example.wms.user.application.domain.enums.UserRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NotificationMapper {
    void insert(Notification notification);
    Notification getAll(UserRole userRole);
}

package com.example.wms.outbound.application.port.in;

import com.example.wms.notification.application.domain.Notification;

public interface CreateOutboundAssignUseCase {
    Notification createOutboundAssign(Long outboundPlanId);
}

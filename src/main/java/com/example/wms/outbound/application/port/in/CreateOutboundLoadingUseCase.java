package com.example.wms.outbound.application.port.in;

import com.example.wms.notification.application.domain.Notification;

public interface CreateOutboundLoadingUseCase {
    Notification createOutboundLoading(Long outboundPlanId);
}

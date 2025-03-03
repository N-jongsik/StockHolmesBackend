package com.example.wms.outbound.application.port.out;

import com.example.wms.outbound.application.domain.OutboundPlan;

public interface DeleteOutboundLoadingPort {
    void deleteOutboundLoading(Long outboundId);
    void updateOutboundPlanStatus(OutboundPlan outboundPlan);
}

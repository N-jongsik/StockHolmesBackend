package com.example.wms.outbound.application.port.out;

import com.example.wms.outbound.application.domain.OutboundPlan;

public interface DeleteOutboundPackingPort {
    void deleteOutboundPacking(Long outboundId);
    void updateOutboundPlanStatus(OutboundPlan outboundPlan);
}

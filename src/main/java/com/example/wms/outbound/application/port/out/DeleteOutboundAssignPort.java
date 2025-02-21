package com.example.wms.outbound.application.port.out;

import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;

public interface DeleteOutboundAssignPort {
    Outbound selectOutboundAssign(Long outboundId);
    void deleteOutboundAssign(Long outboundId);
    void updateOutboundPlanStatus(OutboundPlan outboundPlan);
}

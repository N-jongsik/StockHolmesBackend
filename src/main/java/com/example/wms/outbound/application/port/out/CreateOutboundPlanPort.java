package com.example.wms.outbound.application.port.out;

import com.example.wms.outbound.application.domain.OutboundPlan;

public interface CreateOutboundPlanPort {
    void save(OutboundPlan outboundPlan);
}

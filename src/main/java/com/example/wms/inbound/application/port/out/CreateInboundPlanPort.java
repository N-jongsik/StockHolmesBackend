package com.example.wms.inbound.application.port.out;

import com.example.wms.inbound.application.domain.Inbound;

public interface CreateInboundPlanPort {
    void save(Inbound inbound);
}

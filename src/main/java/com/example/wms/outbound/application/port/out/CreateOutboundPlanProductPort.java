package com.example.wms.outbound.application.port.out;

import com.example.wms.outbound.application.domain.OutboundPlanProduct;

import java.util.List;

public interface CreateOutboundPlanProductPort {
    void saveAll(List<OutboundPlanProduct> outboundPlanProductList);
}

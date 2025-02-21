package com.example.wms.inbound.application.port.in;

import com.example.wms.inbound.adapter.in.dto.request.InboundReqDto;
import com.example.wms.order.application.domain.Order;

public interface CreateInboundPlanUseCase {
    Long createInboundPlan(InboundReqDto inboundReqDto);
    public void createInboundSchedule(Order order);
}

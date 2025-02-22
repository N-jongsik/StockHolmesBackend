package com.example.wms.outbound.application.port.in;

import com.example.wms.notification.application.domain.Notification;
import com.example.wms.outbound.adapter.in.dto.OutboundLotDTO;
import com.example.wms.outbound.application.domain.OutboundPlanProduct;

import java.util.List;

public interface CreateOutboundAssignUseCase {
    Notification createOutboundAssign(String worker, Long outboundPlanId);
    //List<OutboundLotDTO> processLots(List<OutboundPlanProduct> outboundPlanProducts, Long outboundId);
    List<OutboundLotDTO> processCurrentDayLots(String worker);
}

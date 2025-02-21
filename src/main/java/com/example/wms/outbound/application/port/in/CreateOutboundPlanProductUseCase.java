package com.example.wms.outbound.application.port.in;

import com.example.wms.notification.application.domain.Notification;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;

import java.util.List;

public interface CreateOutboundPlanProductUseCase {
    Notification createOutboundPlanProduct(Long outboundPlanId, List<ProductInfoDto> productInfoDtoList);
}

package com.example.wms.outbound.application.port.out;

import com.example.wms.outbound.adapter.in.dto.ABCAnalysisDataDto;

import java.util.List;

public interface OutboundPlanProductPort {
    List<ABCAnalysisDataDto> getRequiredQuantitiesPerProduct();
}

package com.example.wms.outbound.adapter.in.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class OutboundPlanRequestDto {
    private LocalDate outboundScheduleDate;

    private LocalDate planDate;

    private String productionPlanNumber;

    List<ProductInfoDto> productList;
}

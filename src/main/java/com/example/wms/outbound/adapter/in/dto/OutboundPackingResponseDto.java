package com.example.wms.outbound.adapter.in.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboundPackingResponseDto {
    private Long outboundId;

    private Long outboundPlanId;

    private String process; //plan

    private String outboundScheduleNumber; //plan

    private String outboundAssignNumber;

    private String outboundPickingNumber;

    private String outboundPackingNumber;

    private LocalDate outboundPackingDate;

    private String productionPlanNumber; //plan

    private LocalDate planDate; //plan

    private List<ProductInfoDto> productList;
}

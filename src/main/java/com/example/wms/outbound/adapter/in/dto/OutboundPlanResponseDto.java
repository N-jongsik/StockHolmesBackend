package com.example.wms.outbound.adapter.in.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboundPlanResponseDto {
    private Long outboundPlanId;

    private String process;

    private String outboundScheduleNumber;

    private LocalDate outboundScheduleDate;

    private String productionPlanNumber;

    private LocalDate planDate;

    private List<ProductInfoDto> productList;

}

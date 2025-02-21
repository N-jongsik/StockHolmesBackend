package com.example.wms.dashboard.adapter.in.dto;

import lombok.Data;

@Data
public class OutboundStatusResponseDto {
    private Integer outboundSchedule;
    private Integer outboundAssign;
    private Integer outboundPicking;
    private Integer outboundPacking;
    private Integer outboundLoading;
}

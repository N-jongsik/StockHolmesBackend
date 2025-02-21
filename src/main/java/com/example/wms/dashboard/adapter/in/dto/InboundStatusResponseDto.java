package com.example.wms.dashboard.adapter.in.dto;

import lombok.Data;

@Data
public class InboundStatusResponseDto {
    private Integer inboundSchedule;
    private Integer inboundCheck;
    private Integer inboundPutAway;
}

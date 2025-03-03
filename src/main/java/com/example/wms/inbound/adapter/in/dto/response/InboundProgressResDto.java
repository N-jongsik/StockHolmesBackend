package com.example.wms.inbound.adapter.in.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InboundProgressResDto {
    private List<InboundProgressDetailDto> scheduleList;
    private List<InboundProgressDetailDto> checkList;
    private List<InboundProgressDetailDto> putAwayList;
}

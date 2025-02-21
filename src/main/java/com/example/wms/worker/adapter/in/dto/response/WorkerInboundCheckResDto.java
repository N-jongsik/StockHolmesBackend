package com.example.wms.worker.adapter.in.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkerInboundCheckResDto {

    private Long inboundId;
    private String checkNumber;
    private List<WorkerInboundCheckLotResDto> lotList;
}

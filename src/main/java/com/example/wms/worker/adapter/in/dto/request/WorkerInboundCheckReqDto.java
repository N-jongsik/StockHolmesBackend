package com.example.wms.worker.adapter.in.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkerInboundCheckReqDto {
    private List<WorkerInboundCheckProductReqDto> productList;
}

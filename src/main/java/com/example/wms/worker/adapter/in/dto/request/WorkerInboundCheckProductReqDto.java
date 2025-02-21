package com.example.wms.worker.adapter.in.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkerInboundCheckProductReqDto {

    private Long productId;
    private Boolean isDefective;
}

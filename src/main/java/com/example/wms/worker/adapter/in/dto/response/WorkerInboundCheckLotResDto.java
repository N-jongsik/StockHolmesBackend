package com.example.wms.worker.adapter.in.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkerInboundCheckLotResDto {

    private Long lotId;
    private String lotCode;
    private Long productId;
    private String productName;
    private String productCode;
    private String binCode;
}

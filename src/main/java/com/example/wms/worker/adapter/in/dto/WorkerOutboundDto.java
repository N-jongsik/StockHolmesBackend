package com.example.wms.worker.adapter.in.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkerOutboundDto {
    private String binCode; // 빈 코드
    private String zone; // 창고 내 구역 (A~F)
    private Integer aisle; // (1~6)
    private Integer rowNum; // (1~6)
    private Integer floor; // 랙의 층(1~6)
    private String productName;
    private String productCode;
    private Integer productCount;
    private String outboundPickingNumber;
}

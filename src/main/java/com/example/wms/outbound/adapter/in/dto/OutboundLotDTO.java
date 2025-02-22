package com.example.wms.outbound.adapter.in.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboundLotDTO {
    private Long outboundId;
    private Long outboundPlanId;
    private String outboundAssignNumber;
    private List<LotLocation> lotLocations;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LotLocation {
        private Long lotId;
        private Long binId;
        private String binCode;
        private String zone; // 창고 내 구역 (A~F)
        private Integer aisle; // (1~6)
        private Integer rowNum; // (1~6)
        private Integer floor; // 랙의 층(1~6)
        private String productName;
        private String productCode;
        private String productImage;
    }
}

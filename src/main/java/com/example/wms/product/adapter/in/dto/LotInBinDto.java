package com.example.wms.product.adapter.in.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LotInBinDto {
    private Long lotId; // 로트 고유 ID
    private String lotNumber; // 로트 번호
    private Long productId; // 제품 ID
    private Long binId; // 빈 ID
    private String status; // 로트 상태 (입고, 출고중, 출고완료)
    private Long inboundId;
    private Long outboundId;
    private ProductInBinDto productInBinDto;
}

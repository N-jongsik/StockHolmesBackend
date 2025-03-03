package com.example.wms.product.application.domain;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Lot {
    private Long lotId; // 로트 고유 ID
    private String lotNumber; // 로트 번호
    private Long productId; // 제품 ID
    private Long binId; // 빈 ID
    private LotStatus status; // 로트 상태 (입고, 출고중, 출고완료)
    private Long inboundId;
    private Long outboundId;
}



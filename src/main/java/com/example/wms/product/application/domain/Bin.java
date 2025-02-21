package com.example.wms.product.application.domain;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Bin {
    private Long binId; // 빈 고유 ID
    private String binCode; // 빈 코드
    private String zone; // 창고 내 구역 (A~F)
    private Integer aisle; // (1~6)
    private Integer rowNum; // (1~6)
    private Integer floor; // 랙의 층(1~6)
    private Integer amount; // 현재 이 BIN에 들어있는 LOT 갯수(1~6)
}

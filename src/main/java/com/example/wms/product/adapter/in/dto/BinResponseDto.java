package com.example.wms.product.adapter.in.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BinResponseDto {
    private Long binId; // 빈 고유 ID
    private String binCode; // 빈 코드// 빈 우선순위
    private String zone; // 창고 내 구역 (A~F)
    private Integer aisle; // (1~6)
    private Integer row; // (1~6)
    private Integer floor; // 랙의 층(1~6)
    private Integer amount;
    private List<LotInBinDto> lotList;
}

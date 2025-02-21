package com.example.wms.outbound.adapter.in.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ABCAnalysisDataDto {
    private Long productId;
    private Integer totalRequiredQuantity;
}

package com.example.wms.outbound.adapter.in.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfoDto {
    private Long productId;

    private String productCode;

    private String productName;

    private Integer productCount;
}

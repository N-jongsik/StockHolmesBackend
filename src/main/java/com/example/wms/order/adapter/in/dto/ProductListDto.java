package com.example.wms.order.adapter.in.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductListDto {
    private Long productId;

    private String productName;

    private Integer productCount;

    private String productCode;
}

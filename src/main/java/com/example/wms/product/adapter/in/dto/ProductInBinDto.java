package com.example.wms.product.adapter.in.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductInBinDto {
    private String productCode;
    private String productName;
    private String productImageUrl;
}

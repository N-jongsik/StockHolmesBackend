package com.example.wms.order.adapter.in.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductInSupplierDto {
    private Long productId;
    private Long supplierId;
    private String productCode;
    private String productName;
}

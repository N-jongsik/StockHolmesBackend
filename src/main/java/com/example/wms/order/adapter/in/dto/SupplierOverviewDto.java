package com.example.wms.order.adapter.in.dto;

import lombok.Data;

@Data
public class SupplierOverviewDto {
    private Long supplierId; // 공급자 고유 ID
    private String supplierName; // 공급자 이름
}

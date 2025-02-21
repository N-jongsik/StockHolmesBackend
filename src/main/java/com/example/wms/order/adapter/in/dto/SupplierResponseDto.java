package com.example.wms.order.adapter.in.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierResponseDto {
    private Long supplierId; // 공급자 고유 ID
    private String supplierName; // 공급자 이름
    private String businessNumber; // 사업자 등록 번호
    private String representativeName; // 대표자 이름
    private String address; // 공급자 주소
    private String supplierPhone; // 공급자 전화번호
    private String managerPhone;
    private List<ProductInSupplierDto> productList;
}

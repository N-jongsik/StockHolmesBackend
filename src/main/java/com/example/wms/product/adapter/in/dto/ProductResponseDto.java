package com.example.wms.product.adapter.in.dto;

import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductResponseDto {
    private Long productId; // 제품 고유 ID
    private String productCode; // 제품 코드
    private String productName; // 제품 이름
    private Integer purchasePrice; // 구매 가격
    private Integer salePrice; // 판매 가격
    private Long productCount;
    private Integer availableQuantity;
    private String supplierName; // 공급자 이름
    private String category; // 카테고리
    private Integer threshold; // 최소 LOT 수량
    private Integer leadTime; // 이 품목이 납품업체로부터 납품될 때 까지 걸리는 시간
    private String locationBinCode;
    private String abcGrade;
}

package com.example.wms.product.application.domain;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Setter
public class Product {
    private Long productId; // 제품 고유 ID
    private String productCode; // 제품 코드
    private String productName; // 제품 이름
    private Integer purchasePrice; // 구매 가격
    private Integer salePrice; // 판매 가격
    private Integer lotUnit; // 로트당 제품 수량
    private Long supplierId; // 공급자 ID
    private Integer stockLotCount; // 재고 로트 수량
    private String category; // 카테고리
    private Integer threshold; // 최소 LOT 수량
    private Integer leadTime; // 이 품목이 납품업체로부터 납품될 때 까지 걸리는 시간
    private String locationBinCode;
    private String abcGrade;
}
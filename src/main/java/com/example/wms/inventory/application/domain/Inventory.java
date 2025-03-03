package com.example.wms.inventory.application.domain;

import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Inventory {
    private Long inventoryId; // 재고 고유 ID
    private Long productId; // 제품 ID
    private Integer availableQuantity;// 사용 가능한 수량
    private LocalDate lastUpdated;
}

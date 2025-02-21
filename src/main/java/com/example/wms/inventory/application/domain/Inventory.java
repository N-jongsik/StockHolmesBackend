package com.example.wms.inventory.application.domain;

import com.example.wms.infrastructure.entity.BaseEntity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Inventory extends BaseEntity {
    private Long inventoryId; // 재고 고유 ID
    private Long productId; // 제품 ID
    private Integer availableQuantity;// 사용 가능한 수량
    private LocalDate lastUpdated;
}

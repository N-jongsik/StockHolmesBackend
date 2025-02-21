package com.example.wms.outbound.application.domain;

import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class OutboundPlanProduct {
    private Long outboundPlanProductId;

    private Long outboundPlanId;

    private Long productId; // 제품 ID

    private Integer requiredQuantity; // 필요 수량

    private Integer stockUsedQuantity; // 재고에서 사용한 수량

    private Integer orderQuantity; // 주문된 수량 (재고 부족 시 발주)

    private String status; // 계획 상태 (예: 대기, 진행 중, 완료)
}

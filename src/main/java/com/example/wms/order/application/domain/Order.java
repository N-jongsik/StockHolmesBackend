package com.example.wms.order.application.domain;

import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Order {
    private Long orderId;
    private Long supplierId;
    private LocalDate orderDate; //주문 날짜
    private LocalDate inboundDate; //입고 날짜
    private Boolean isApproved;
    private Boolean isDelayed;
    private String orderNumber;
    private String orderStatus;
    private Long dailyPlanId;
    private Boolean isReturnOrder;
}

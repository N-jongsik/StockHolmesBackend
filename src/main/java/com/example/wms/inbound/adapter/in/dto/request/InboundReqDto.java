package com.example.wms.inbound.adapter.in.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InboundReqDto {

    private LocalDate scheduleDate; // order의 inboundDate 값을 저장하는 것임

    private Long orderId; // 발주 id (자동 생성)

    private Long supplierId; // 구매처 id

    private String orderNumber; // 발주 번호

    private LocalDateTime orderDate; // 발주 일자
}

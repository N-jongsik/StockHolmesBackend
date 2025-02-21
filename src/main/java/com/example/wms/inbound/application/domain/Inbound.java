package com.example.wms.inbound.application.domain;

import com.example.wms.infrastructure.entity.BaseEntity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inbound {
    private Long inboundId;
    private String inboundStatus; // 입고 상태
    private String scheduleNumber; // 입하 예정 번호
    private LocalDate scheduleDate;

    private String checkNumber; // 입고 검사 번호
    private LocalDate checkDate;

    private String putAwayNumber; // 입고 적치 번호
    private LocalDate putAwayDate;

    private Long orderId; // 발주 번호
    private Long supplierId; // 공급 업체 번호
}

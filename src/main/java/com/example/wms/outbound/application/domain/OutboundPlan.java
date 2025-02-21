package com.example.wms.outbound.application.domain;

import com.example.wms.infrastructure.entity.BaseEntity;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class OutboundPlan {
    private Long outboundPlanId;

    private LocalDate planDate; // 창고에서 나가는 날짜

    private String status; // 계획 상태 (예: 대기, 진행 중, 완료)

    private String outboundScheduleNumber; // 출고 예정 번호

    private LocalDate outboundScheduleDate; // 출고 예정이 생긴 날짜

    private String productionPlanNumber;
    //생산계획번호 (앱을 통해 주문이 들어오면 값이 있는 상태로 wms로 온다 or 관리자가 직접 생성한거임)
}

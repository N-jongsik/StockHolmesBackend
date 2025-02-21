package com.example.wms.outbound.application.service;

import com.example.wms.outbound.adapter.in.dto.OutboundPlanRequestDto;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.in.CreateOutboundPlanUseCase;
import com.example.wms.outbound.application.port.out.CalculateOsNumberPort;
import com.example.wms.outbound.application.port.out.CreateOutboundPlanPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CreateOutboundPlanService implements CreateOutboundPlanUseCase {
    private final CreateOutboundPlanPort createOutboundPlanPort;
    private final CalculateOsNumberPort calculateOsNumberPort;

    @Transactional
    @Override
    public Long createOutbound(OutboundPlanRequestDto outboundPlanRequestDto) {

        String currentDate = LocalDate.now().toString().replace("-", ""); // 예: 2025-02-10 -> 20250210

        // DB에서 가장 큰 outboundScheduleNumber 조회
        String maxOutboundNumber = calculateOsNumberPort.findMaxOutboundScheduleNumber();
        String nextNumber = "0000";

        if (maxOutboundNumber != null) {
            // 마지막 4자리 숫자 추출해서 + 1
            String lastNumberStr = maxOutboundNumber.substring(maxOutboundNumber.length() - 4);
            int lastNumber = Integer.parseInt(lastNumberStr);
            nextNumber = String.format("%04d", lastNumber + 1);
        }

        String osNumber = "OS" + currentDate + nextNumber;

        // 엔티티로 변환하기
        OutboundPlan outboundPlan = OutboundPlan.builder()
                .planDate(outboundPlanRequestDto.getPlanDate())
                .status("출고예정")
                .outboundScheduleNumber(osNumber)
                .outboundScheduleDate(outboundPlanRequestDto.getOutboundScheduleDate())
                .productionPlanNumber(outboundPlanRequestDto.getProductionPlanNumber())
                .build();

        // DB에 저장하기
        createOutboundPlanPort.save(outboundPlan);
        return outboundPlan.getOutboundPlanId();
    }
}

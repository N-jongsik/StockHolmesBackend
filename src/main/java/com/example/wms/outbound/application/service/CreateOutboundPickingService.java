package com.example.wms.outbound.application.service;

import com.example.wms.infrastructure.exception.DuplicatedException;
import com.example.wms.notification.application.domain.Notification;
import com.example.wms.notification.application.port.out.NotificationPort;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.in.CreateOutboundPickingUseCase;
import com.example.wms.outbound.application.port.out.CreateOutboundPickingPort;
import com.example.wms.outbound.application.port.out.GetOutboundPickingPort;
import com.example.wms.user.application.domain.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CreateOutboundPickingService implements CreateOutboundPickingUseCase {

    private final CreateOutboundPickingPort createOutboundPickingPort;
    private final NotificationPort notificationPort;
    private final GetOutboundPickingPort getOutboundPickingPort;

    @Override
    public Notification createOutboundPicking(Long outboundPlanId) {

        // 1. 기존 출고 정보 확인
        Outbound existingOutbound = createOutboundPickingPort.findOutboundByPlanId(outboundPlanId);

        if (existingOutbound != null && existingOutbound.getOutboundPickingNumber() != null) {
            // 이미 출고 피킹 있음
            throw new DuplicatedException("이미 출고 피킹이 등록된 상태입니다.");
        }

        String currentDate = LocalDate.now().toString().replace("-", "");
        String maxOutboundPickingNumber = createOutboundPickingPort.findMaxOutboundPickingNumber();
        String nextNumber = "0000";

        if (maxOutboundPickingNumber != null) {
            String lastNumberStr = maxOutboundPickingNumber.substring(maxOutboundPickingNumber.length() - 4);
            int lastNumber = Integer.parseInt(lastNumberStr);
            nextNumber = String.format("%04d", lastNumber + 1);
        }

        String oiNumber = "OI" + currentDate + nextNumber;

        existingOutbound.setOutboundPickingNumber(oiNumber);
        existingOutbound.setOutboundPickingDate(LocalDate.now());

        createOutboundPickingPort.createOutboundPicking(existingOutbound);

        // outboundPlan status 바꿔주기
        OutboundPlan outboundPlan = getOutboundPickingPort.findOutboundPlanByOutboundPlanId(outboundPlanId);
        createOutboundPickingPort.updateOutboundPlanStatus(outboundPlan);

        Notification notification = Notification.builder()
                .content("출고 피킹이 등록되었습니다.")
                .event("출고 피킹")
                .userRole(UserRole.ROLE_ADMIN)
                .build();

        notificationPort.save(notification);
        return notification;
    }
}

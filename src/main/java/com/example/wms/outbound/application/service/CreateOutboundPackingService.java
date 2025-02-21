package com.example.wms.outbound.application.service;

import com.example.wms.infrastructure.exception.DuplicatedException;
import com.example.wms.notification.application.domain.Notification;
import com.example.wms.notification.application.port.in.NotificationUseCase;
import com.example.wms.notification.application.port.out.NotificationPort;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.in.CreateOutboundPackingUseCase;
import com.example.wms.outbound.application.port.out.CreateOutboundPackingPort;
import com.example.wms.outbound.application.port.out.GetOutboundPackingPort;
import com.example.wms.user.application.domain.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CreateOutboundPackingService implements CreateOutboundPackingUseCase {

    private final CreateOutboundPackingPort createOutboundPackingPort;
    private final GetOutboundPackingPort getOutboundPackingPort;
    private final NotificationPort notificationPort;

    @Override
    public Notification createOutboundPacking(Long outboundPlanId) {
        // 1. 기존 출고 정보 확인
        Outbound existingOutbound = createOutboundPackingPort.findOutboundByPlanId(outboundPlanId);

        if (existingOutbound != null && existingOutbound.getOutboundPackingNumber() != null) {
            // 이미 출고 피킹 있음
            throw new DuplicatedException("이미 출고 피킹이 등록된 상태입니다.");
        }

        String currentDate = LocalDate.now().toString().replace("-", "");
        String maxOutboundPackingNumber = createOutboundPackingPort.findMaxOutboundPackingNumber();
        String nextNumber = "0000";

        if (maxOutboundPackingNumber != null) {
            String lastNumberStr = maxOutboundPackingNumber.substring(maxOutboundPackingNumber.length() - 4);
            int lastNumber = Integer.parseInt(lastNumberStr);
            nextNumber = String.format("%04d", lastNumber + 1);
        }

        String opNumber = "OP" + currentDate + nextNumber;

        existingOutbound.setOutboundPackingNumber(opNumber);
        existingOutbound.setOutboundPackingDate(LocalDate.now());

        createOutboundPackingPort.createOutboundPacking(existingOutbound);

        // outboundPlan status 바꿔주기
        OutboundPlan outboundPlan = getOutboundPackingPort.findOutboundPlanByOutboundPlanId(outboundPlanId);
        createOutboundPackingPort.updateOutboundPlanStatus(outboundPlan);

        Notification notification = Notification.builder()
                .content("출고 패킹이 등록되었습니다.")
                .event("출고 패킹")
                .userRole(UserRole.ROLE_ADMIN)
                .build();

        notificationPort.save(notification);
        return notification;
    }
}

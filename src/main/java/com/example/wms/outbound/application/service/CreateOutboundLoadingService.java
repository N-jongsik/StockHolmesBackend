package com.example.wms.outbound.application.service;

import com.example.wms.infrastructure.exception.DuplicatedException;
import com.example.wms.notification.application.domain.Notification;
import com.example.wms.notification.application.port.out.NotificationPort;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.in.CreateOutboundLoadingUseCase;
import com.example.wms.outbound.application.port.out.CreateOutboundLoadingPort;
import com.example.wms.outbound.application.port.out.GetOutboundLoadingPort;
import com.example.wms.user.application.domain.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CreateOutboundLoadingService implements CreateOutboundLoadingUseCase {

    private final CreateOutboundLoadingPort createOutboundLoadingPort;
    private final GetOutboundLoadingPort getOutboundLoadingPort;
    private final NotificationPort notificationPort;

    @Override
    public Notification createOutboundLoading(Long outboundPlanId) {
        // 1. 기존 출고 정보 확인
        Outbound existingOutbound = createOutboundLoadingPort.findOutboundByPlanId(outboundPlanId);

        if (existingOutbound != null && existingOutbound.getOutboundLoadingNumber() != null) {
            // 이미 출고 피킹 있음
            throw new DuplicatedException("이미 출고 로딩이 등록된 상태입니다.");
        }

        String currentDate = LocalDate.now().toString().replace("-", "");
        String maxOutboundLoadingNumber = createOutboundLoadingPort.findMaxOutboundLoadingNumber();
        String nextNumber = "0000";

        if (maxOutboundLoadingNumber != null) {
            String lastNumberStr = maxOutboundLoadingNumber.substring(maxOutboundLoadingNumber.length() - 4);
            int lastNumber = Integer.parseInt(lastNumberStr);
            nextNumber = String.format("%04d", lastNumber + 1);
        }

        String olNumber = "OL" + currentDate + nextNumber;

        existingOutbound.setOutboundLoadingNumber(olNumber);
        existingOutbound.setOutboundLoadingDate(LocalDate.now());

        createOutboundLoadingPort.createOutboundLoading(existingOutbound);

        // outboundPlan status 바꿔주기
        OutboundPlan outboundPlan = getOutboundLoadingPort.findOutboundPlanByOutboundPlanId(outboundPlanId);
        createOutboundLoadingPort.updateOutboundPlanStatus(outboundPlan);

        Notification notification = Notification.builder()
                .content("출고 로딩이 등록되었습니다.")
                .event("출고 로딩")
                .userRole(UserRole.ROLE_ADMIN)
                .build();

        notificationPort.save(notification);
        return notification;
    }
}

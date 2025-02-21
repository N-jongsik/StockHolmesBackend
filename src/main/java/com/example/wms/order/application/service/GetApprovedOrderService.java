package com.example.wms.order.application.service;

import com.example.wms.inbound.application.domain.Inbound;
import com.example.wms.inbound.application.port.in.InboundUseCase;
import com.example.wms.inbound.application.port.out.AssignInboundNumberPort;
import com.example.wms.inbound.application.port.out.InboundPort;
import com.example.wms.order.application.domain.Order;
import com.example.wms.order.application.port.in.GetApprovedOrderUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class GetApprovedOrderService implements GetApprovedOrderUseCase {

    private final InboundUseCase inboundService;
    private final InboundPort inboundPort;
    private final AssignInboundNumberPort assignInboundNumberPort;

    // 발주 등록 시 호출
    // 호출 전 발주 등록 부분에서 orderStatus를 "APPROVED" 로 변경해야 함
    @Transactional
    @Override
    public void processOrderApproval(Order order) {
        if (order.getIsApproved()) {
            Inbound inboundPlan = Inbound.builder()
                    .inboundStatus("입하예정")
                    .scheduleNumber(makeNumber("IS"))
                    .scheduleDate(order.getInboundDate())
                    .orderId(order.getOrderId())
                    .supplierId(order.getSupplierId())
                    .build();

            inboundPort.save(inboundPlan);
        }

    }

    private String makeNumber(String format) {
        String currentDate = LocalDate.now().toString().replace("-","");
        String number = switch (format) {
            case "IS" -> assignInboundNumberPort.findMaxISNumber();
            case "IC" -> assignInboundNumberPort.findMaxICNumber();
            case "PA" -> assignInboundNumberPort.findMaxPANumber();
            default -> null;
        };

        String nextNumber = "0000";

        if (number != null) {
            int lastNumber = Integer.parseInt(number.substring(number.length()-4));
            nextNumber = String.format("%04d", lastNumber+1);
        }

        return format + currentDate + nextNumber;
    }

}

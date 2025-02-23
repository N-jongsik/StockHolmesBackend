package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.request.InboundReqDto;
import com.example.wms.inbound.application.domain.Inbound;
import com.example.wms.inbound.application.port.in.CreateInboundPlanUseCase;
import com.example.wms.inbound.application.port.out.AssignInboundNumberPort;
import com.example.wms.inbound.application.port.out.CreateInboundPlanPort;
import com.example.wms.order.application.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CreateInboundPlanService implements CreateInboundPlanUseCase {

    private final CreateInboundPlanPort createInboundPlanPort;
    private final AssignInboundNumberPort assignInboundNumberPort;

    @Transactional
    @Override
    public Long createInboundPlan(InboundReqDto inboundReqDto) {

        Inbound inboundPlan = Inbound.builder()
                .scheduleNumber(makeNumber("IS"))
                .inboundStatus("입하예정")
                .scheduleDate(inboundReqDto.getScheduleDate())
                .orderId(inboundReqDto.getOrderId())
                .supplierId(inboundReqDto.getSupplierId())
                .build();

        createInboundPlanPort.save(inboundPlan);
        return inboundPlan.getInboundId();
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

    @Transactional
    @Override
    public void createInboundSchedule(Order order) {
        Inbound inboundPlan = Inbound.builder()
                .inboundStatus("입하예정")
                .scheduleNumber(makeNumber("IS"))
                .scheduleDate(order.getInboundDate())
                .orderId(order.getOrderId())
                .supplierId(order.getSupplierId())
                .build();

        createInboundPlanPort.save(inboundPlan);
    }



}

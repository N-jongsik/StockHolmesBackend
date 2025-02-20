package com.example.wms.order.application.service;

import com.example.wms.inbound.application.domain.Inbound;
import com.example.wms.inbound.application.port.out.AssignInboundNumberPort;
import com.example.wms.inbound.application.port.out.InboundPort;
import com.example.wms.order.adapter.in.dto.ProductListDto;
import com.example.wms.order.application.port.in.UpdateOrderUseCase;
import com.example.wms.order.application.port.out.UpdateOrderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdateOrderService implements UpdateOrderUseCase {

    private final UpdateOrderPort updateOrderPort;
    private final AssignInboundNumberPort assignInboundNumberPort;
    private final InboundPort inboundPort;

    @Override
    public void updateOrder(Long orderId, List<ProductListDto> productList) {
        updateOrderPort.updateOrder(orderId, productList);
    }

    @Override
    public void updateOrderApprove(Long orderId) {
        updateOrderPort.updateOrderApprove(orderId);

        Inbound inboundPlan = Inbound.builder()
                .scheduleNumber(makeNumber("IS"))
                .inboundStatus("입하예정")
                .scheduleDate(LocalDate.now().plusDays(3))
                .orderId(orderId)
                .supplierId(null)
                .build();

        inboundPort.save(inboundPlan);
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

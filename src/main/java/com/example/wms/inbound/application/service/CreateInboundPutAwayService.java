package com.example.wms.inbound.application.service;

import com.example.wms.inbound.application.domain.Inbound;
import com.example.wms.inbound.application.port.in.CreateInboundPutAwayUseCase;
import com.example.wms.inbound.application.port.out.AssignInboundNumberPort;
import com.example.wms.inbound.application.port.out.InboundPort;
import com.example.wms.infrastructure.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CreateInboundPutAwayService implements CreateInboundPutAwayUseCase {

    private final InboundPort inboundPort;
    private final AssignInboundNumberPort assignInboundNumberPort;

    @Override
    public void createPutAway(Long inboundId) {

        Inbound inbound = inboundPort.findById(inboundId);

        if (inbound == null) {
            throw new NotFoundException("Inbound not found with id " + inboundId);
        }

        String putAwayNumber = makeNumber("PA");
        LocalDate putAwayDate = LocalDate.now();
        String inboundStatus = "입고적치";

        inboundPort.updatePA(inboundId, putAwayDate, putAwayNumber, inboundStatus);

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

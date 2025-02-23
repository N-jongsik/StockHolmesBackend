package com.example.wms.inbound.application.service;

import com.example.wms.inbound.application.domain.Inbound;
import com.example.wms.inbound.application.port.in.DeleteInboundCheckUseCase;
import com.example.wms.inbound.application.port.out.InboundPort;
import com.example.wms.infrastructure.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteInboundCheckService implements DeleteInboundCheckUseCase {

    private final InboundPort inboundPort;

    @Override
    public void deleteInboundCheck(Long inboundId) {
        Inbound inbound = inboundPort.findById(inboundId);

        if (inbound == null) {
            throw new NotFoundException("not found with id " + inboundId);
        }

        inboundPort.updateIC(inboundId, null, null, "입하예정");
    }
}

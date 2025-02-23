package com.example.wms.worker.adapter.in;

import com.example.wms.outbound.adapter.in.dto.OutboundLotDTO;
import com.example.wms.outbound.application.port.in.CreateOutboundAssignUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/worker/outbound")
@RequiredArgsConstructor
public class WorkerOutboundController {

    private final CreateOutboundAssignUseCase createOutboundAssignUseCase;

    @GetMapping
    public ResponseEntity<List<OutboundLotDTO>> workerOutbound() {
        String worker = "worker";
        return ResponseEntity.ok().body(createOutboundAssignUseCase.processCurrentDayLots(worker));
    }
}

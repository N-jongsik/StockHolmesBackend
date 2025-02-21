package com.example.wms.inbound.adapter.in;

import com.example.wms.inbound.application.port.in.CreateInboundPutAwayUseCase;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inboundPutAway")
public class InboundPutAwayCreateController {

    private final CreateInboundPutAwayUseCase createInboundPutAwayUseCase;

    @PostMapping("/{inboundId}")
    @Operation(summary = "입고 적치 생성하기", description = "입고 적치를 생성합니다.")
    public ResponseEntity<Void> createInboundPutAway(@PathVariable Long inboundId)
    {
        createInboundPutAwayUseCase.createPutAway(inboundId);
         return ResponseEntity.status(201).build();
    }


}

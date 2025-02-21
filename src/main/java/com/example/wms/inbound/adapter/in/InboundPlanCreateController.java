package com.example.wms.inbound.adapter.in;

import com.example.wms.inbound.adapter.in.dto.request.InboundReqDto;
import com.example.wms.inbound.application.port.in.CreateInboundPlanUseCase;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inbound")
public class InboundPlanCreateController {

    private final CreateInboundPlanUseCase createInboundPlanUseCase;

    @PostMapping
    @Operation(summary = "입하 예정 생성하기", description = "InboundReqDto를 입력하여 입고 예정을 생성합니다.")
    public ResponseEntity<Void> createInbound(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "입고 예정 생성 요청 DTO",
                    required = true,
                    content = @Content(schema = @Schema(implementation = InboundReqDto.class))
            )
            @RequestBody InboundReqDto inboundReqDto) {
        Long inboundId = createInboundPlanUseCase.createInboundPlan(inboundReqDto); // 수동 생성
        return ResponseEntity.status(201).build();
    }



}

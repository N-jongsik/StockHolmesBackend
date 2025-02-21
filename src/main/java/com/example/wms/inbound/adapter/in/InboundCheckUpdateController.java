package com.example.wms.inbound.adapter.in;

import com.example.wms.inbound.adapter.in.dto.request.InboundCheckUpdateReqDto;
import com.example.wms.inbound.application.port.in.UpdateInboundCheckUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inboundCheck")
public class InboundCheckUpdateController {
    private final UpdateInboundCheckUseCase updateInboundCheckUseCase;

    @PutMapping("/{inboundId}")
    @Operation(summary = "입하 검사 수정하기", description= "해당 inboundId의 입하 검사를 수정합니다.")
    public ResponseEntity<Void> updateOutboundPlan(
            @Parameter(name = "inboundId", in = ParameterIn.PATH, required = true, description = "수정할 입하 검사 ID", example = "123")
            @PathVariable Long inboundId, @RequestBody InboundCheckUpdateReqDto inboundCheckUpdateReqDto) {
        updateInboundCheckUseCase.updateInboundCheck(inboundId, inboundCheckUpdateReqDto);
        return ResponseEntity.ok().build();
    }
}

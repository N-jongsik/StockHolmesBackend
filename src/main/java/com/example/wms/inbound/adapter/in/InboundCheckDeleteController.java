package com.example.wms.inbound.adapter.in;

import com.example.wms.inbound.application.port.in.DeleteInboundCheckUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inboundCheck")
public class InboundCheckDeleteController {

    private final DeleteInboundCheckUseCase deleteInboundCheckUseCase;

    @DeleteMapping("/{inboundId}")
    @Operation(summary ="입하 검사 삭제하기", description = "해당 inboundId의 입하 검사를 삭제합니다.")
    public ResponseEntity<Void> deleteInboundCheck(
            @Parameter(name = "inboundId", in = ParameterIn.PATH, required = true, description = "삭제할 입하 검사 ID", example = "123")
            @PathVariable Long inboundId) {
        deleteInboundCheckUseCase.deleteInboundCheck(inboundId);
        return ResponseEntity.noContent().build();
    }
}

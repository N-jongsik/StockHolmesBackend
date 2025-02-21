package com.example.wms.inbound.adapter.in;

import com.example.wms.inbound.application.port.in.DeleteInboundPlanUseCase;
import com.example.wms.inbound.application.port.in.InboundUseCase;
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
@RequestMapping("/inbound")
public class InboundPlanDeleteController {

    private final DeleteInboundPlanUseCase deleteInboundUseCase;

    @DeleteMapping("/{inboundId}")
    @Operation(summary = "입하 예정 삭제하기", description = "입력한 inboundId에 해당하는 입하 예정을 삭제합니다.")
    public ResponseEntity<Void> deleteInbound(
            @Parameter(name = "inboundId", in = ParameterIn.PATH, required = true, description = "삭제할 입하 예정 ID", example = "123")
            @PathVariable Long inboundId) {
        deleteInboundUseCase.deleteInboundPlan(inboundId);
        return ResponseEntity.noContent().build();
    }

}

package com.example.wms.inbound.adapter.in;


import com.example.wms.inbound.adapter.in.dto.request.InboundCheckReqDto;
import com.example.wms.inbound.application.port.in.CreateInboundCheckUseCase;
import com.example.wms.inbound.application.port.in.InboundUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inboundCheck")
public class InboundCheckCreateController {

    private final CreateInboundCheckUseCase createInboundCheckUseCase;

    @PostMapping("/{inboundId}")
    @Operation(summary = "입하 검사 관리자가 등록하기", description = "입력한 inboundId에 해당하는 데이터를 입하 검사로 등록합니다.")
    public ResponseEntity<Void> createInboundCheck(
            @Parameter(name= "inboundId", in = ParameterIn.PATH, required = true, description = "등록할 입하 검사 ID", example = "123")
            @PathVariable Long inboundId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "입하 검사 생성 요청 DTO",
                    required = true,
                    content = @Content(schema = @Schema(implementation = InboundCheckReqDto.class))
            )
            @RequestBody InboundCheckReqDto inboundCheckReqDto) {
        createInboundCheckUseCase.createInboundCheck(inboundId, inboundCheckReqDto);
        return ResponseEntity.status(201).build();
    }

}

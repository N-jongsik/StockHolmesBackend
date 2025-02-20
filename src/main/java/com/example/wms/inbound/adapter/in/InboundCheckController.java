package com.example.wms.inbound.adapter.in;

import com.example.wms.inbound.adapter.in.dto.request.InboundCheckReqDto;
import com.example.wms.inbound.adapter.in.dto.request.InboundCheckUpdateReqDto;
import com.example.wms.inbound.adapter.in.dto.request.InboundCheckWorkerReqDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundResDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundWorkerCheckResDto;
import com.example.wms.inbound.application.port.in.InboundUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inboundCheck")
public class InboundCheckController {

    private final InboundUseCase inboundUseCase;

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
        inboundUseCase.createInboundCheck(inboundId, inboundCheckReqDto);
        return ResponseEntity.status(201).build();
    }

    @GetMapping
    @Operation(summary = "입하 검사 조회하기" , description = "입하검사번호와 시작일, 종료일을 입력해 입하 검사 데이터를 검색 조건에 따라 조회합니다.")
    public ResponseEntity<Page<InboundResDto>> getInboundCheck(
            @RequestParam(value = "number", required = false) String inboundCheckNumber,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(inboundUseCase.getFilteredInboundCheck(inboundCheckNumber, startDate, endDate, pageable));
    }

    @PutMapping("/{inboundId}")
    @Operation(summary = "입하 검사 수정하기", description= "해당 inboundId의 입하 검사를 수정합니다.")
    public ResponseEntity<Void> updateOutboundPlan(
            @Parameter(name = "inboundId", in = ParameterIn.PATH, required = true, description = "수정할 입하 검사 ID", example = "123")
            @PathVariable Long inboundId, @RequestBody InboundCheckUpdateReqDto inboundCheckUpdateReqDto) {
        inboundUseCase.updateInboundCheck(inboundId, inboundCheckUpdateReqDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{inboundId}")
    @Operation(summary ="입하 검사 삭제하기", description = "해당 inboundId의 입하 검사를 삭제합니다.")
    public ResponseEntity<Void> deleteInboundCheck(
            @Parameter(name = "inboundId", in = ParameterIn.PATH, required = true, description = "삭제할 입하 검사 ID", example = "123")
            @PathVariable Long inboundId) {
        inboundUseCase.deleteInboundCheck(inboundId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/check")
    @Operation(summary="입하 검사 작업자가 등록하기", description = "InboundCheckWorkerReqDto를 입하 검사를 등록합니다.")
    public ResponseEntity<InboundWorkerCheckResDto> checkInbound(@RequestBody List<InboundCheckWorkerReqDto> checkRequests) {
        InboundWorkerCheckResDto response = inboundUseCase.createInboundCheckByWorker(checkRequests);
        return ResponseEntity.ok(response);
    }
}

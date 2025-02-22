package com.example.wms.inbound.adapter.in;


import com.example.wms.inbound.adapter.in.dto.request.InboundCheckReqDto;
import com.example.wms.inbound.adapter.in.dto.request.InboundCheckUpdateReqDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundResDto;
import com.example.wms.inbound.application.port.in.CreateInboundCheckUseCase;
import com.example.wms.inbound.application.port.in.DeleteInboundCheckUseCase;
import com.example.wms.inbound.application.port.in.GetInboundCheckUseCase;
import com.example.wms.inbound.application.port.in.UpdateInboundCheckUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inboundCheck")
public class InboundCheckController {

    private final CreateInboundCheckUseCase createInboundCheckUseCase;
    private final GetInboundCheckUseCase getInboundCheckUseCase;
    private final UpdateInboundCheckUseCase updateInboundCheckUseCase;


    @PostMapping("/{inboundId}")
    @Operation(summary = "입하 검사 관리자가 등록하기", description = "입력한 inboundId에 해당하는 데이터를 입하 검사로 등록합니다.")
    public ResponseEntity<Void> createInboundCheck(
            @Parameter(name= "inboundId", in = ParameterIn.PATH, required = true, description = "등록할 입하 검사 ID", example = "123")
            @PathVariable Long inboundId,
            @RequestBody InboundCheckReqDto inboundCheckReqDto) {
        createInboundCheckUseCase.createInboundCheck(inboundId, inboundCheckReqDto);
        return ResponseEntity.status(201).build();
    }

    private final DeleteInboundCheckUseCase deleteInboundCheckUseCase;

    @DeleteMapping("/{inboundId}")
    @Operation(summary ="입하 검사 삭제하기", description = "해당 inboundId의 입하 검사를 삭제합니다.")
    public ResponseEntity<Void> deleteInboundCheck(
            @Parameter(name = "inboundId", in = ParameterIn.PATH, required = true, description = "삭제할 입하 검사 ID", example = "123")
            @PathVariable Long inboundId) {
        deleteInboundCheckUseCase.deleteInboundCheck(inboundId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "입하 검사 조회하기" , description = "입하검사번호와 시작일, 종료일을 입력해 입하 검사 데이터를 검색 조건에 따라 조회합니다.")
    public ResponseEntity<Page<InboundResDto>> getInboundCheck(
            @RequestParam(value = "number", required = false) String inboundCheckNumber,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(getInboundCheckUseCase.getFilteredInboundCheck(inboundCheckNumber, startDate, endDate, pageable));
    }


    @PutMapping("/{inboundId}")
    @Operation(summary = "입하 검사 수정하기", description= "해당 inboundId의 입하 검사를 수정합니다.")
    public ResponseEntity<Void> updateOutboundPlan(
            @Parameter(name = "inboundId", in = ParameterIn.PATH, required = true, description = "수정할 입하 검사 ID", example = "123")
            @PathVariable Long inboundId, @RequestBody InboundCheckUpdateReqDto inboundCheckUpdateReqDto) {
        updateInboundCheckUseCase.updateInboundCheck(inboundId, inboundCheckUpdateReqDto);
        return ResponseEntity.ok().build();
    }


}

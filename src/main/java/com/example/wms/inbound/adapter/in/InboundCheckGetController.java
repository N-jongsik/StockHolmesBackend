package com.example.wms.inbound.adapter.in;

import com.example.wms.inbound.adapter.in.dto.response.InboundResDto;
import com.example.wms.inbound.application.port.in.GetInboundCheckUseCase;
import io.swagger.v3.oas.annotations.Operation;
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
public class InboundCheckGetController {

    private final GetInboundCheckUseCase getInboundCheckUseCase;


    @GetMapping
    @Operation(summary = "입하 검사 조회하기" , description = "입하검사번호와 시작일, 종료일을 입력해 입하 검사 데이터를 검색 조건에 따라 조회합니다.")
    public ResponseEntity<Page<InboundResDto>> getInboundCheck(
            @RequestParam(value = "number", required = false) String inboundCheckNumber,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(getInboundCheckUseCase.getFilteredInboundCheck(inboundCheckNumber, startDate, endDate, pageable));
    }

}

package com.example.wms.inbound.adapter.in;

import com.example.wms.inbound.adapter.in.dto.request.InboundPutAwayReqDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundPutAwayResDto;
import com.example.wms.inbound.application.port.in.InboundUseCase;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inboundPutAway")
public class InboundPutAwayController {

    private final InboundUseCase inboundUseCase;

    @PostMapping("/{inboundId}")
    @Operation(summary = "입고 적치 생성하기", description = "입고 적치를 생성합니다.")
    public ResponseEntity<Void> createInboundPutAway(@PathVariable Long inboundId)
    {
         inboundUseCase.putAway(inboundId);
         return ResponseEntity.status(201).build();
    }

    @GetMapping
    @Operation(summary = "입고 적치 목록 조회하기" , description = "필터링 값이 없으면 전체 조회합니다.")
    public ResponseEntity<Page<InboundPutAwayResDto>> getInboundPutAway(
            @RequestParam(value = "number", required = false) String inboundPutAwayNumber,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(inboundUseCase.getFilteredPutAway(inboundPutAwayNumber, startDate, endDate, pageable));
    }
}

package com.example.wms.inbound.adapter.in;

import com.example.wms.inbound.adapter.in.dto.response.InboundPutAwayResDto;
import com.example.wms.inbound.application.port.in.CreateInboundPutAwayUseCase;
import com.example.wms.inbound.application.port.in.GetInboundPutAwayUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/inboundPutAway")
@Tag(name = "입고 적치 API")
public class InboundPutAwayCreateController {

    private final CreateInboundPutAwayUseCase createInboundPutAwayUseCase;
    private final GetInboundPutAwayUseCase getInboundPutAwayUseCase;


    @PostMapping("/{inboundId}")
    @Operation(summary = "입고 적치 생성하기", description = "입고 적치를 생성합니다.")
    public ResponseEntity<Void> createInboundPutAway(@PathVariable Long inboundId)
    {
        createInboundPutAwayUseCase.createPutAway(inboundId);
         return ResponseEntity.status(201).build();
    }

    @GetMapping
    @Operation(summary = "입고 적치 목록 조회하기" , description = "필터링 값이 없으면 전체 조회합니다.")
    public ResponseEntity<Page<InboundPutAwayResDto>> getInboundPutAway(
            @RequestParam(value = "number", required = false) String inboundPutAwayNumber,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(getInboundPutAwayUseCase.getFilteredPutAway(inboundPutAwayNumber, startDate, endDate, pageable));
    }


}

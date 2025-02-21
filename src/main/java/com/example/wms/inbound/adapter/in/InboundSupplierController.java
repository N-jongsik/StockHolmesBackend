package com.example.wms.inbound.adapter.in;

import com.example.wms.inbound.adapter.in.dto.response.SupplierInboundResDto;
import com.example.wms.inbound.application.service.InboundService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inboundSupplier")
public class InboundSupplierController {

    private final InboundService inboundService;

    @GetMapping
    @Operation(summary = "납품업체별 입고 목록 조회하기" , description = "필터링 값이 없으면 전체 조회합니다.")
    public ResponseEntity<Page<SupplierInboundResDto>> getAllInboundBySupplier(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<SupplierInboundResDto> response = inboundService.getAllInboundBySupplierWithPagination(startDate, endDate, pageable);

        return ResponseEntity.ok(response);
    }
}

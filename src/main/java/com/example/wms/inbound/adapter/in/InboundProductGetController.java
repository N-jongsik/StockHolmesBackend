package com.example.wms.inbound.adapter.in;

import com.example.wms.inbound.adapter.in.dto.response.ProductInboundResDto;
import com.example.wms.inbound.application.port.in.GetAllInboundByProductWithPaginationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
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
@RequestMapping("/inboundProduct")
@Tag(name = "품목별 입고 조회 API")
public class InboundProductGetController {

    private final GetAllInboundByProductWithPaginationUseCase getAllInboundByProductWithPaginationUseCase;
    @GetMapping
    @Operation(summary = "품목별 입고 목록 조회하기" , description = "필터링 값이 없으면 전체 조회합니다.")
    public ResponseEntity<Page<ProductInboundResDto>> getAllInboundByProduct(

            @Parameter(name ="startDate", in = ParameterIn.QUERY, description = "시작 날짜")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @Parameter(name = "endDate", in = ParameterIn.QUERY, description = "종료 날짜")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(getAllInboundByProductWithPaginationUseCase.getAllInboundByProductWithPagination(startDate, endDate, pageable));
    }

}

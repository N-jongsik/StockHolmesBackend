package com.example.wms.inbound.adapter.in;

import com.example.wms.inbound.adapter.in.dto.response.InboundResDto;
import com.example.wms.inbound.application.port.in.GetInboundPlanUseCase;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
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
@RequestMapping("/inbound")
public class InboundPlanGetController {

    private final GetInboundPlanUseCase getInboundPlanUseCase;

    @GetMapping
    @Operation(summary = "입하 예정 조회하기" , description = "입하예정번호와 시작일, 종료일을 입력해 입하 예정 데이터를 검색 조건에 따라 조회합니다.")
    @PageableAsQueryParam
    public ResponseEntity<Page<InboundResDto>> getInboundPlans(

            @RequestParam(value = "number", required = false) String inboundScheduleNumber,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

            @ParameterObject Pageable pageable)  // 자동으로 page, size, sort를 문서화
    {

        return ResponseEntity.ok(getInboundPlanUseCase.getFilteredInboundPlans(inboundScheduleNumber, startDate, endDate, pageable));
    }

}

package com.example.wms.inbound.adapter.in;

import com.example.wms.inbound.adapter.in.dto.request.InboundReqDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundResDto;
import com.example.wms.inbound.application.port.in.CreateInboundPlanUseCase;
import com.example.wms.inbound.application.port.in.DeleteInboundPlanUseCase;
import com.example.wms.inbound.application.port.in.GetInboundPlanUseCase;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inbound")
@Tag(name = "입하 예정 API")

public class InboundPlanCreateController {

    private final CreateInboundPlanUseCase createInboundPlanUseCase;
    private final DeleteInboundPlanUseCase deleteInboundUseCase;
    private final GetInboundPlanUseCase getInboundPlanUseCase;


    @PostMapping
    @Operation(summary = "입하 예정 생성하기", description = "InboundReqDto를 입력하여 입고 예정을 생성합니다.")
    public ResponseEntity<Void> createInbound(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "입고 예정 생성 요청 DTO",
                    required = true,
                    content = @Content(schema = @Schema(implementation = InboundReqDto.class))
            )
            @RequestBody InboundReqDto inboundReqDto) {
        Long inboundId = createInboundPlanUseCase.createInboundPlan(inboundReqDto); // 수동 생성
        return ResponseEntity.status(201).build();
    }


    @DeleteMapping("/{inboundId}")
    @Operation(summary = "입하 예정 삭제하기", description = "입력한 inboundId에 해당하는 입하 예정을 삭제합니다.")
    public ResponseEntity<Void> deleteInbound(
            @Parameter(name = "inboundId", in = ParameterIn.PATH, required = true, description = "삭제할 입하 예정 ID", example = "123")
            @PathVariable Long inboundId) {
        deleteInboundUseCase.deleteInboundPlan(inboundId);
        return ResponseEntity.noContent().build();
    }


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

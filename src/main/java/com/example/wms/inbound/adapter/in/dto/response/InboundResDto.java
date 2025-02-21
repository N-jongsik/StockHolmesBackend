package com.example.wms.inbound.adapter.in.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Schema(description = "입고 조회 결과 응답 DTO")
public class InboundResDto {

    @Schema(description = "입고 id", example = "12")
    private Long inboundId;

    @Schema(description = "입고 과정", example = "입하예정")
    private String inboundStatus;

    @Schema(description = "생성 날짜", example = "2025-02-16")
    private LocalDate createdAt;

    @Schema(description = "입하예정번호", example = "IS202502010003")
    private String scheduleNumber;

    @Schema(description = "입하예정날짜", example = "2025-02-13")
    private LocalDate scheduleDate;

    @Schema(description = "입하 검사 번호", example = "IC202502030001")
    private String checkNumber;

    @Schema(description = "입하예정날짜", example = "2025-02-13")
    private LocalDate checkDate;

    @Schema(description = "발주Id", example = "13")
    private Long orderId;

    @Schema(description = "발주번호", example = "OR202502030004")
    private String orderNumber;

    @Schema(description = "발주날짜시간", example = "2025-02-13T15:39:30")
    private LocalDate orderDate;

    @Schema(description = "납품업체 Id", example = "13")
    private Long supplierId;

    @Schema(description = "납품업체명", example = "hyundai tire")
    private String supplierName;

    @Schema(description = "입고 품목 리스트")
    private List<InboundProductDto> productList;
}


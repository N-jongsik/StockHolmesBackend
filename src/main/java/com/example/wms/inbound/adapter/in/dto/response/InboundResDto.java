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

public class InboundResDto {

    private Long inboundId;

    private String inboundStatus;

    private LocalDate createdAt;

    private String scheduleNumber;

    private LocalDate scheduleDate;

    private String checkNumber;

    private LocalDate checkDate;

    private Long orderId;

    private String orderNumber;

    private LocalDate orderDate;

    private Long supplierId;

    private String supplierName;

    private List<InboundProductDto> productList;

    private List<LotResDto> lotList;
}


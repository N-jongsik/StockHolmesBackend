package com.example.wms.inbound.adapter.in.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "입고 품목")
public class InboundProductDto {

    private Long productId;

    private String productCode;

    private String productName;

    private Long productCount;

    private Long stockLotCount;

    private Long defectiveCount;
}

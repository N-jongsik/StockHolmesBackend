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

    @Schema(description = "품목 id", example = "123")
    private Long productId;

    @Schema(description = "품목코드", example = "P1234")
    private String productCode;

    @Schema(description = "품목명", example = "tire")
    private String productName;

    @Schema(description = "품목마다 발주한 수량", example = "30")
    private Long productCount;

    @Schema(description = "품목 lot 수량", example = "3")
    private Long stockLotCount;

    @Schema(description = "품목별 불량 Lot 수량", example = "5")
    private Long defectiveCount;
}

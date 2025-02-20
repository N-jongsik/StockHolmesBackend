package com.example.wms.inbound.adapter.in.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "입고 적치 생성 요청 dto")
public class InboundPutAwayReqDto {

    @Schema(description = "품목 id", example = "100")
    private Long productId;

    @Schema(description = "품목 별 lot 단위 개수", example = "3")
    private Long lotCount;

}

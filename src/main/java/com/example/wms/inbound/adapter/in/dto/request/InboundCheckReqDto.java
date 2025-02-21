package com.example.wms.inbound.adapter.in.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "입하 검사 등록 요청 dto")
public class InboundCheckReqDto {

    @Schema(description = "한 입하 예정에 속한 입하검사 품목 리스트")
    private List<InboundCheckedProductReqDto> checkedProductList;
}

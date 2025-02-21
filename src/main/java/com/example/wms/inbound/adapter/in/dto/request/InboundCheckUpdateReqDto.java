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
@Schema(description = "입하 검사 수정 요청 DTO")
public class InboundCheckUpdateReqDto {

    @Schema(description = "입하 검사 수정 항목들")
    private List<InboundCheckedProductReqDto> checkedProductList;
}

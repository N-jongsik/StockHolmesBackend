package com.example.wms.inbound.adapter.in.dto.response;

import com.example.wms.product.adapter.in.dto.LotInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "작업자 입하 검사 결과 응답 DTO")
public class InboundWorkerCheckResDto {

    @Schema(description = "입하검사 번호", example = "IC202502170021")
    private String checkNumber;

    @Schema(description = "입하 검사 완료한 항목들")
    private List<LotInfoDto> lots;
}

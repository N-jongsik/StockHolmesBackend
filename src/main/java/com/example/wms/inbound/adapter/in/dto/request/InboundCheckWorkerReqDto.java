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
@Schema(description = "작업자 입하 검사 생성 요청 DTO")
public class InboundCheckWorkerReqDto {

    private Long productId;

    private Boolean isDefective;

    private String scheduleNumber;
}

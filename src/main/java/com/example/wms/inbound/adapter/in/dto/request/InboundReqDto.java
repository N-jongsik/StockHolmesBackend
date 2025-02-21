package com.example.wms.inbound.adapter.in.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "입하 예정 생성 요청 DTO")
public class InboundReqDto {

    @Schema(description = "입하 예정일 (Order의 inboundDate 값이 저장됨)", example = "2025-02-15")
    private LocalDate scheduleDate; // order의 inboundDate 값을 저장하는 것임

    @Schema(description = "발주 ID (자동 생성)", example = "1001")
    private Long orderId; // 발주 id (자동 생성)

    @Schema(description = "구매처 ID (공급업체)", example = "5001")
    private Long supplierId; // 구매처 id

    @Schema(description = "발주 번호", example = "ORD202502170001")
    private String orderNumber; // 발주 번호

    @Schema(description = "발주 일자 (YYYY-MM-DD HH:mm:ss)", example = "2024-02-17T15:30:00")
    private LocalDateTime orderDate; // 발주 일자
}

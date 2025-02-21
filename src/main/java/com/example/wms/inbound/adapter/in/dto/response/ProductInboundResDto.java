package com.example.wms.inbound.adapter.in.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductInboundResDto {
    private Long productId;
    private String productCode;
    private String productName;
    private Integer productCount;
    private Integer defectiveCount;
    private List<InboundDetailDto> inboundList;
}

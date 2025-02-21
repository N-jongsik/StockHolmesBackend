package com.example.wms.product.adapter.out.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FlatBinDto {
    private Long binId;
    private String binCode;
    private String zone;
    private Integer aisle;
    private Integer row;
    private Integer floor;
    private Integer amount;
    private Long lotId;
    private String lotNumber;
    private Long productId;
    private String status;
    private Long inboundId;
    private Long outboundId;
    private String productCode;
    private String productName;
}


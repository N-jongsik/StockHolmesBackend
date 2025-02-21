package com.example.wms.inventory.adapter.in.dto;

import lombok.Data;

@Data
public class ThresholdUpdateRequestDto {
    private Long productId;
    private Integer threshold;
}

package com.example.wms.order.adapter.in.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {
    private Long supplierId;

    private Long outboundPlanId;

    private List<ProductListDto> productList;
}

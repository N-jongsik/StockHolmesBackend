package com.example.wms.order.adapter.in.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {
    private Long orderId;

    private String orderNumber;

    private String orderStatus;

    private String supplierName;

    private Long supplierId;

    private LocalDate orderDate;

    private Boolean isApproved;

    private Boolean isReturnOrder;

    private LocalDate deliveryDeadline;

    private List<ProductListDto> productList;


}

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
public class InboundPutAwayResDto {
    private Long inboundId;
    private String inboundStatus;
    private String createdAt;
    private String scheduleNumber;
    private String inboundCheckNumber;
    private String putAwayNumber;
    private String putAwayDate;
    private Long orderId;
    private String orderNumber;
    private String orderDate;
    private Long supplierId;
    private String supplierName;
    private List<LotResDto> lotList;
}

package com.example.wms.outbound.application.domain;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Outbound {
    private Long outboundId;

    private Long outboundPlanId;

    private String outboundAssignNumber;

    private LocalDate outboundAssignDate;

    private String outboundPickingNumber;

    private LocalDate outboundPickingDate;

    private String outboundPackingNumber;

    private LocalDate outboundPackingDate;

    private String outboundLoadingNumber;

    private LocalDate outboundLoadingDate;
}

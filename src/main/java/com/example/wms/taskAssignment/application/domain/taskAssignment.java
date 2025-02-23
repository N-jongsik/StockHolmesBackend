package com.example.wms.taskAssignment.application.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class taskAssignment {
    private Long taskId;

    private Long userId;

    private String taskType;

    private String status;

    private LocalDateTime assignedDate;

    private LocalDateTime completedDate;

    private Long supplierOrderId;

    private Long lotId;

    private int quantity; // 입고/출고 수량

}

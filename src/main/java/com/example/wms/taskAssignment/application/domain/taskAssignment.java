package com.example.wms.taskAssignment.application.domain;

import com.example.wms.infrastructure.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class taskAssignment extends BaseEntity {
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

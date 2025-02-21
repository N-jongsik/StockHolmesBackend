package com.example.wms.infrastructure.entity;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public abstract class BaseEntity {

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
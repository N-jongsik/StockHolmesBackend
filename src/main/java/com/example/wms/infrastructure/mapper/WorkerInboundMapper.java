package com.example.wms.infrastructure.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

@Mapper
public interface WorkerInboundMapper {

    Integer countFilteredWorkerInboundList(@Param("startDate")LocalDate startDate, @Param("endDate") LocalDate endDate);
}

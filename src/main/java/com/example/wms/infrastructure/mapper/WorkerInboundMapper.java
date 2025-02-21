package com.example.wms.infrastructure.mapper;

import com.example.wms.worker.adapter.in.dto.response.WorkerInboundResDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface WorkerInboundMapper {

    Integer countFilteredWorkerInboundList(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    List<WorkerInboundResDto> findFilteredWorkerInboundList(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("pageable")Pageable pageable);
}

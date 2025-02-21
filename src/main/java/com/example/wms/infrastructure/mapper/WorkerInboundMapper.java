package com.example.wms.infrastructure.mapper;

import com.example.wms.worker.adapter.in.dto.response.WorkerInboundResDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface WorkerInboundMapper {

    List<WorkerInboundResDto> findFilteredWorkerInboundList(@Param("todayDate") LocalDate todayDate);
}

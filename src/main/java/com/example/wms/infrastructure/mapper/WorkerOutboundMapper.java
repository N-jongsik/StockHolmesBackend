package com.example.wms.infrastructure.mapper;

import com.example.wms.outbound.application.domain.OutboundPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface WorkerOutboundMapper {
    List<OutboundPlan> findOutboundPlanByDate(@Param("today") LocalDate today);
}

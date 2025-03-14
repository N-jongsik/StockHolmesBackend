package com.example.wms.infrastructure.mapper;


import com.example.wms.inbound.adapter.in.dto.response.InboundAllProductDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface InboundPlanMapper {
    List<InboundAllProductDto> findInboundPlanFilteringWithPagination(@Param("inboundScheduleNumber") String inboundScheduleNumber, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("pageable") Pageable pageable);
    Integer countAllInboundPlanFiltering(@Param("inboundScheduleNumber") String inboundScheduleNumber, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}

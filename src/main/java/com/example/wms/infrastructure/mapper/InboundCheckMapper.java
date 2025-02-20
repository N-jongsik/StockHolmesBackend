package com.example.wms.infrastructure.mapper;

import com.example.wms.inbound.adapter.in.dto.response.InboundAllProductDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface InboundCheckMapper {

    List<InboundAllProductDto> findInboundCheckFilteringWithPagination(@Param("inboundCheckNumber") String inboundCheckNumber, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("pageable") Pageable pageable);
    Integer countAllInboundCheckFiltering(@Param("inboundCheckNumber") String inboundCheckNumber, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}

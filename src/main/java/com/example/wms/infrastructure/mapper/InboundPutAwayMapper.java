package com.example.wms.infrastructure.mapper;

import com.example.wms.inbound.adapter.in.dto.response.InboundPutAwayAllProductDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundPutAwayResDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface InboundPutAwayMapper {
    List<InboundPutAwayAllProductDto> findInboundPutAwayFilteringWithPagination(@Param("inboundPutAwayNumber") String inboundPutAwayNumber, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("pageable") Pageable pageable);
    Integer countFilteredPutAway(@Param("inboundPutAwayNumber") String inboundPutAwayNumber, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}

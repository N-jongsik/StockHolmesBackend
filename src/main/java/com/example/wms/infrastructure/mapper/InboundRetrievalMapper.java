package com.example.wms.infrastructure.mapper;

import com.example.wms.inbound.adapter.in.dto.response.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface InboundRetrievalMapper {
    List<InboundProductDto> findInboundProductListByOrderId(Long orderId);

    List<InboundAllProductDto> findInboundProductListWithPagination(@Param("pageable") Pageable pageable);

    List<InboundAllProductDto> findInboundFilteringWithPagination(@Param("inboundScheduleNumber") String inboundScheduleNumber, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("pageable") Pageable pageable);

    Integer countAllInboundPlan();


    List<ProductInboundResDto> findAllInboundByProductWithPagination(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("pageable") Pageable pageable);
    List<SupplierInboundResDto> findAllInboundBySupplierWithPagination(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("pageable") Pageable pageable);
    List<InboundProgressDetailDto> findAllInboundProgressWithPagination(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("pageable") Pageable pageable);
}
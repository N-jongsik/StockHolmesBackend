package com.example.wms.infrastructure.mapper;

import com.example.wms.outbound.adapter.in.dto.OutboundLoadingResponseDto;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface OutboundLoadingMapper {
    // 출고 로딩 등록
    void insertOutboundLoading(
            @Param("outboundId") Long outboundId,
            @Param("outboundLoadingNumber") String outboundLoadingNumber,
            @Param("outboundLoadingDate") LocalDate outboundLoadingDate
    );

    String findMaxOutboundLoadingNumber();

    Outbound findOutboundByPlanId(@Param("outboundPlanId") Long outboundPlanId);

    // 출고 로딩 삭제
    void deleteOutboundLoading(@Param("outboundId") Long outboundId);

    // 출고 로딩 수정
    void updateOutboundLoading(@Param("outboundId") Long outboundId, @Param("outboundLoadingDate") LocalDate outboundLoadingDate);

    // 출고 로딩 조회
    List<OutboundLoadingResponseDto> findOutboundLoadingFilteringWithPageNation(
            @Param("outboundLoadingNumber") String outboundLoadingNumber,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("pageable") Pageable pageable);

    OutboundPlan findOutboundPlanByOutboundPlanId(@Param("outboundPlanId") Long outboundPlanId);

    Outbound findOutboundByOutboundId(@Param("outboundId") Long outboundId);

    Integer countLoading(@Param("outboundLoadingNumber") String outboundLoadingNumber,
                         @Param("startDate") LocalDate startDate,
                         @Param("endDate") LocalDate endDate);

    void updateOutboundPlanStatus(@Param("outboundPlanId") Long outboundPlanId,@Param("status") String status);
}

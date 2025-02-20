package com.example.wms.infrastructure.mapper;

import com.example.wms.outbound.adapter.in.dto.OutboundPickingResponseDto;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface OutboundPickingMapper {

    void insertOutboundPicking(
            @Param("outboundId") Long outboundId,
            @Param("outboundPickingNumber") String outboundPickingNumber,
            @Param("outboundPickingDate") LocalDate outboundPickingDate
    );

    String findMaxOutboundPickingNumber();

    Outbound findOutboundByPlanId(@Param("outboundPlanId") Long outboundPlanId);

    // 출고 피킹 삭제
    void deleteOutboundPicking(@Param("outboundId") Long outboundId);

    // 출고 피킹 수정
    void updateOutboundPicking(@Param("outboundId") Long outboundId, @Param("outboundPickingDate") LocalDate outboundPickingDate);

    // 출고 피킹 조회
    List<OutboundPickingResponseDto> findOutboundPickingFilteringWithPageNation(
            @Param("outboundPickingNumber") String outboundPickingNumber,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("pageable") Pageable pageable);

    OutboundPlan findOutboundPlanByOutboundPlanId(@Param("outboundPlanId") Long outboundPlanId);

    Integer countAllPicking(
            @Param("outboundPickingNumber") String outboundPickingNumber,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    void updateOutboundPlanStatus(@Param("outboundPlanId") Long outboundPlanId,@Param("status") String status);

    Outbound findOutboundByOutboundId(@Param("outboundId") Long outboundId);
}

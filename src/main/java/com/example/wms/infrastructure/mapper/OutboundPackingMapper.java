package com.example.wms.infrastructure.mapper;

import com.example.wms.outbound.adapter.in.dto.OutboundPackingResponseDto;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface OutboundPackingMapper {
    void insertOutboundPacking(
            @Param("outboundId") Long outboundId,
            @Param("outboundPackingNumber") String outboundPackingNumber,
            @Param("outboundPackingDate") LocalDate outboundPackingDate
    );

    String findMaxOutboundPackingNumber();

    Outbound findOutboundByPlanId(@Param("outboundPlanId") Long outboundPlanId);

    // 출고 패킹 삭제
    void deleteOutboundPacking(@Param("outboundId") Long outboundId);

    // 출고 패킹 수정
    void updateOutboundPacking(@Param("outboundId") Long outboundId, @Param("outboundPackingDate") LocalDate outboundPackingDate);

    // 출고 패킹 조회
    List<OutboundPackingResponseDto> findOutboundPackingFilteringWithPageNation(
            @Param("outboundPackingNumber") String outboundPackingNumber,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("pageable") Pageable pageable);

    OutboundPlan findOutboundPlanByOutboundPlanId(@Param("outboundPlanId") Long outboundPlanId);

    Integer countPacking(@Param("outboundPackingNumber") String outboundPackingNumber,
                         @Param("startDate") LocalDate startDate,
                         @Param("endDate") LocalDate endDate);

    void updateOutboundPlanStatus(@Param("outboundPlanId") Long outboundPlanId,@Param("status") String status);

    Outbound findOutboundByOutboundId(@Param("outboundId") Long outboundId);
}

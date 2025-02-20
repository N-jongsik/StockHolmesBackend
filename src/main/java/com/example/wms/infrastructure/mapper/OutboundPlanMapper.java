package com.example.wms.infrastructure.mapper;

import com.example.wms.outbound.adapter.in.dto.OutboundPlanRequestDto;
import com.example.wms.outbound.adapter.in.dto.OutboundPlanResponseDto;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.domain.OutboundPlanProduct;
import org.apache.ibatis.annotations.*;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Mapper
public interface OutboundPlanMapper {
    
    // outboundPlan 저장하기
    void insert(OutboundPlan outboundPlan);

    // outboundPlan 삭제하기
    void deleteById(@Param("outboundPlanId") Long outboundPlanId);

    // outboundPlanProduct 삭제하기
    void deleteOutboundPlanProductsByPlanId(@Param("outboundPlanId") Long outboundPlanId);

    // outbound 삭제하기
    void deleteOutboundById(@Param("outboundPlanId") Long outboundPlanId);

    // outboundPlan 삭제 시 outboundPlanProduct도 함께 삭제
    @Transactional
    default void deleteOutboundPlanAndProducts(Long outboundPlanId) {
        // outbound_plan_product 삭제
        deleteOutboundPlanProductsByPlanId(outboundPlanId);

        // outbound 삭제
        deleteOutboundById(outboundPlanId);

        // outbound_plan 삭제
        deleteById(outboundPlanId);
    }

    // 출고 계획 수정
    void updateOutboundPlan(@Param("outboundPlanId") Long outboundPlanId, @Param ("outboundPlanRequestDto") OutboundPlanRequestDto outboundPlanRequestDto);

    // 출고 계획 제품 수정
    void updateOutboundPlanProducts(@Param("outboundPlanId") Long outboundPlanId, @Param("productList") List<ProductInfoDto> productList);

    List<OutboundPlanResponseDto> findOutboundPlanFilteringWithPageNation(
            @Param("outboundScheduleNumber") String outboundScheduleNumber,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("pageable") Pageable pageable
    );

    Integer countAllOutboundPlanFiltering(String outboundScheduleNumber, LocalDate startDate, LocalDate endDate);

    Optional<Outbound> findOutboundByOutboundPlanId(@Param("outboundPlanId") Long outboundPlanId);

    Optional<OutboundPlan> findOutboundPlanByOutboundPlanId(@Param("outboundPlanId") Long outboundPlanId);

    void updateOutboundPlanStatus(@Param("outboundPlanId") Long outboundPlanId,@Param("status") String status);

    List<OutboundPlanProduct> findOutboundPlanProductsByPlanId(@Param("outboundPlanId") Long outboundPlanId);
}

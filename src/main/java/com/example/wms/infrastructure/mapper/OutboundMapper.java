package com.example.wms.infrastructure.mapper;

import com.example.wms.outbound.adapter.in.dto.OutboundPlanRequestDto;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlanProduct;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.ibatis.annotations.*;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface OutboundMapper {
    // outbound 테이블 생성
    @Insert("""
        INSERT INTO outbound
        (outbound_plan_id, outbound_assign_number, outbound_assign_date, outbound_picking_number, outbound_picking_date, outbound_packing_number, outbound_packing_date, outbound_loading_number, outbound_loading_date)
        VALUES
        (#{outboundPlanId}, #{outboundAssignNumber}, #{outboundAssignDate}, #{outboundPickingNumber}, #{outboundPickingDate}, #{outboundPackingNumber}, #{outboundPackingDate}, #{outboundLoadingNumber}, #{outboundLoadingDate})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "outboundId")
    int insert(Outbound outbound);

    // outbound 테이블 있을 때
    @Update("""
    UPDATE outbound 
        SET outbound_assign_number = #{outboundAssignNumber},
            outbound_assign_date = #{outboundAssignDate}
        WHERE outbound_id = #{outboundId}
    """)
    void insertOutboundAssign(
            @Param("outboundId") Long outboundId,
            @Param("outboundAssignNumber") String outboundAssignNumber,
            @Param("outboundAssignDate") LocalDate outboundAssignDate
    );


    @Select("""
        SELECT outbound_assign_number FROM outbound ORDER BY outbound_assign_number DESC LIMIT 1
    """)
    String findMaxOutboundAssignNumber();

    @Select("""
        SELECT COUNT(*)
        FROM outbound
        WHERE outbound_plan_id = #{outboundPlanId}
    """)
    int findOutboundAssignByPlanId(@Param("outboundPlanId") Long outboundPlanId);

    @Select("""
        SELECT * 
        FROM outbound
        WHERE outbound_id = #{outboundId}
    """)
    Outbound findOutboundByOutboundId(Long outboundId);

    // 출고 지시 삭제
    @Update("UPDATE outbound SET outbound_assign_date = NULL, outbound_assign_number = NULL WHERE outbound_id = #{outboundId}")
    void deleteOutboundAssign(@Param("outboundId") Long outboundId);

    // 출고 지시 수정
    @Update("UPDATE outbound SET outbound_assign_date = #{outboundAssignDate} WHERE outbound_id = #{outboundId}")
    void updateOutboundAssign(@Param("outboundId") Long outboundId, @Param("outboundAssignDate") LocalDate outboundAssignDate);

    // outboundPlanId 로 outbound하나 찾기
    @Select("""
        SELECT * 
        FROM outbound
        WHERE outbound_plan_id = #{outboundPlanId};
    """)
    Outbound findOutboundByPlanId(@Param("outboundPlanId") Long outboundPlanId);

    @Update("""
        UPDATE outbound_plan
        SET status = #{status}
        WHERE outbound_plan_id = #{outboundPlanId}
    """)
    void updateOutboundPlanStatus(@Param("outboundPlanId") Long outboundPlanId,@Param("status") String status);

}

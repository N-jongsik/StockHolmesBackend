package com.example.wms.infrastructure.mapper;

import com.example.wms.outbound.application.domain.Outbound;
import org.apache.ibatis.annotations.*;
import java.time.LocalDate;

@Mapper
public interface OutboundMapper {
    // outbound 테이블 생성
    int insert(Outbound outbound);

    // outbound 테이블 있을 때
    void insertOutboundAssign(
            @Param("outboundId") Long outboundId,
            @Param("outboundAssignNumber") String outboundAssignNumber,
            @Param("outboundAssignDate") LocalDate outboundAssignDate
    );

    String findMaxOutboundAssignNumber();

    Outbound findOutboundByOutboundId(Long outboundId);

    // 출고 지시 삭제
    void deleteOutboundAssign(@Param("outboundId") Long outboundId);

    // 출고 지시 수정
    void updateOutboundAssign(@Param("outboundId") Long outboundId, @Param("outboundAssignDate") LocalDate outboundAssignDate);

    // outboundPlanId 로 outbound하나 찾기
    Outbound findOutboundByPlanId(@Param("outboundPlanId") Long outboundPlanId);

    void updateOutboundPlanStatus(@Param("outboundPlanId") Long outboundPlanId,@Param("status") String status);
}

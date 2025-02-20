package com.example.wms.infrastructure.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CalculateOsNumberMapper {
    String findMaxOutboundScheduleNumber(); // 가장 큰 outboundScheduleNumber 조회
}

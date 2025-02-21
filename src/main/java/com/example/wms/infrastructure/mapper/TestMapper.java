package com.example.wms.infrastructure.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface TestMapper {

    @Select("SELECT * FROM inbound WHERE check_number IS NOT NULL AND put_away_number IS NULL")
    List<Map<String, Object>> findInboundCheckFilteringWithPagination();
}

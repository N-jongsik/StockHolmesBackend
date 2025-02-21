package com.example.wms.infrastructure.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface InboundNumberMapper {

    String findMaxISNumber();

    String findMaxICNumber();

    String findMaxPANumber();
}

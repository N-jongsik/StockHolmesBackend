package com.example.wms.infrastructure.mapper;

import com.example.wms.product.application.domain.Lot;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LotMapper {
    void updateStatus(Long lotId, String status);
    void save(Lot lot);
}

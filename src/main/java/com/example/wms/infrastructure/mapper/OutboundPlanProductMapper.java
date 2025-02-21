package com.example.wms.infrastructure.mapper;

import com.example.wms.outbound.adapter.in.dto.ABCAnalysisDataDto;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.domain.OutboundPlanProduct;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OutboundPlanProductMapper {
    void batchInsert(@Param("products") List<OutboundPlanProduct> outboundPlanProductList);

    List<ProductInfoDto> findProductInfoByOutboundPlanId(Long outboundPlanId);

    List<ABCAnalysisDataDto> getRequiredQuantitiesPerProduct();
}

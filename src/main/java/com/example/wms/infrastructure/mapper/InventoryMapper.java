package com.example.wms.infrastructure.mapper;

import com.example.wms.inventory.adapter.in.dto.ProductThresholdDto;
import com.example.wms.inventory.adapter.in.dto.ThresholdUpdateRequestDto;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import com.example.wms.product.application.domain.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface InventoryMapper {
    List<ProductInfoDto> findAllProductInventories(@Param("productCode") String productCode, @Param("pageable")Pageable pageable);
    long countAllProductInventories(@Param("productCode") String productCode);
    List<ProductThresholdDto> findAllProductThresholds(@Param("productCode") String productCode, @Param("pageable") Pageable pageable);
    void updateInventory(Long productId, Integer lotCount);
    Product findByProductId(Long productId);
    int updateThreshold(ThresholdUpdateRequestDto thresholdUpdateRequestDto);
    Integer findAvailableQuantity(@Param("productId") Long productId);
    void updateInventoryQuantity(@Param("productId") Long productId, @Param("availableQuantity") Integer availableQuantity);
}

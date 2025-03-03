package com.example.wms.inventory.application.port.out;

import com.example.wms.inventory.adapter.in.dto.ProductThresholdDto;
import com.example.wms.inventory.adapter.in.dto.ThresholdUpdateRequestDto;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import com.example.wms.product.application.domain.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InventoryPort {
    List<ProductInfoDto> findAllProductInventories(String productCode, Pageable pageable);
    long countAllProductInventories(String productCode);

    List<ProductThresholdDto> findAllProductThresholds(String productCode, Pageable pageable);
    void updateInventory(Long productId, Integer lotCount);

    int updateThreshold(ThresholdUpdateRequestDto thresholdUpdateRequestDto);
    Product findByProductId(Long productId);

    Integer findAvailableQuantityByProductId(Long productId);

    void updateInventoryAvailableQuantity(Long productId, Integer availableQuantity);
}

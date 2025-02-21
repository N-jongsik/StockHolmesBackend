package com.example.wms.inventory.application.port.in;

import com.example.wms.inventory.adapter.in.dto.ProductThresholdDto;
import com.example.wms.inventory.adapter.in.dto.ThresholdUpdateRequestDto;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import com.example.wms.product.application.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InventoryUseCase {

    Page<ProductInfoDto> getAllProductInventories(String productCode, Pageable pageable);
    Page<ProductThresholdDto> getAllProductThresholds(String productCode, Pageable pageable);
    Product updateThreshold(ThresholdUpdateRequestDto thresholdUpdateRequestDto);
    Integer findAvailableQuantity(Long productId);
}

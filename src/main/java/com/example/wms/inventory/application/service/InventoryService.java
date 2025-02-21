package com.example.wms.inventory.application.service;

import com.example.wms.infrastructure.pagination.util.PageableUtils;
import com.example.wms.inventory.adapter.in.dto.ProductThresholdDto;
import com.example.wms.inventory.adapter.in.dto.ThresholdUpdateRequestDto;
import com.example.wms.inventory.application.port.in.InventoryUseCase;
import com.example.wms.inventory.application.port.out.InventoryPort;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import com.example.wms.product.application.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InventoryService implements InventoryUseCase {

    private final InventoryPort inventoryPort;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductInfoDto> getAllProductInventories(String productCode, Pageable pageable) {
        Map<String, String> fieldMapping = new HashMap<>();
        fieldMapping.put("productCount", "available_quantity"); // 명시적으로 매핑

        Pageable safePageable = PageableUtils.convertToSafePageableStrict(pageable, ProductInfoDto.class, fieldMapping);

        List<ProductInfoDto> productInventoryList = inventoryPort.findAllProductInventories(productCode, safePageable);
        long count = inventoryPort.countAllProductInventories(productCode);

        return new PageImpl<>(productInventoryList, safePageable, count);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductThresholdDto> getAllProductThresholds(String productCode, Pageable pageable) {
        Pageable safePageable = PageableUtils.convertToSafePageableStrict(pageable, ProductThresholdDto.class);

        List<ProductThresholdDto> productThresholdDtoList = inventoryPort.findAllProductThresholds(productCode, safePageable);
        long count = inventoryPort.countAllProductInventories(productCode);

        return new PageImpl<>(productThresholdDtoList, safePageable, count);
    }

    @Override
    @Transactional
    public Product updateThreshold(ThresholdUpdateRequestDto thresholdUpdateRequestDto) {
        int updatedCount = inventoryPort.updateThreshold(thresholdUpdateRequestDto);
        Product existingProduct = inventoryPort.findByProductId(thresholdUpdateRequestDto.getProductId());
        return existingProduct;
    }

    @Override
    public Integer findAvailableQuantity(Long productId) {
        return 0;
    }

}

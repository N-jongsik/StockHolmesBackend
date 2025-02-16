package com.example.wms.inventory.application.port.out;

import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InventoryPort {
    List<ProductInfoDto> findAllProductInventories(String productCode, Pageable pageable);
    long countAllProductInventories(String productCode);
    void updateInventory(Long productId, Integer lotCount);
}

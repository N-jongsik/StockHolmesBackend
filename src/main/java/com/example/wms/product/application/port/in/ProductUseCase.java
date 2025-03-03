package com.example.wms.product.application.port.in;

import com.example.wms.product.adapter.in.dto.ProductOverviewDto;
import com.example.wms.product.adapter.in.dto.ProductResponseDto;
import com.example.wms.product.application.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductUseCase {
    void performABCAnalysis();
    void assignLocationBinCode();
    Page<ProductResponseDto> getAllProducts(String productCode, Pageable pageable);
    List<ProductOverviewDto> getProductOverview();
}

package com.example.wms.product.application.port.out;

import com.example.wms.inbound.adapter.in.dto.request.InboundPutAwayReqDto;
import com.example.wms.product.adapter.in.dto.ProductOverviewDto;
import com.example.wms.product.adapter.in.dto.ProductResponseDto;
import com.example.wms.product.application.domain.Product;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductPort {

    List<Product> getAllProducts();
    void updateABCGrades(Long productId, String abcGrade);
    void updateBinCode(Long productId, String binCode);
    List<ProductResponseDto> findProductWithPagination(String productCode, Pageable pageable);
    long countAllProducts(String productCode);
    List<ProductOverviewDto> findProductOverview();
    Product findById(Long productId);
    String getLocationBinCode(Long productId);
    void updateRequiredQuantity(Long productId, Integer lotCount);
    Long getSupplierId(Long productId);
    List<Product> findPutAwayProductsByInboundId(Long inboundId);
}

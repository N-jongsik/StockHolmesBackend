package com.example.wms.infrastructure.mapper;

import com.example.wms.product.adapter.in.dto.ProductOverviewDto;
import com.example.wms.product.adapter.in.dto.ProductResponseDto;
import com.example.wms.product.application.domain.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface ProductMapper {
    void save(Product product);
    List<Product> getAllProducts();
    void updateABCGrade(@Param("productId") Long productId, @Param("abcGrade") String abcGrade);
    void updateBinLocation(@Param("productId") Long productId, @Param("binCode") String locationBinCode);

    List<ProductResponseDto> findProductWithPagination(@Param("productCode") String productCode, @Param("pageable") Pageable pageable);
    long countAllProducts(@Param("productCode") String productCode);

    @Select("SELECT product_id, product_name, product_code FROM product")
    List<ProductOverviewDto> selectProductOverview();

    Product findById(@Param("productId") Long productId);
    String getLocationBinCode(@Param("productId") Long productId);
    void updateRequiredQuantity(@Param("productId") Long productId, @Param("lotCount") Integer lotCount);

    Long getSupplierIdByProductId(Long productId);
}

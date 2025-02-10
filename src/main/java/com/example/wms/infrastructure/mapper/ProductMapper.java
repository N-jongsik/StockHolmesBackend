package com.example.wms.infrastructure.mapper;

import com.example.wms.product.application.domain.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductMapper {
    @Select("SELECT * FROM product")
    List<Product> getAllProducts();
    void updateABCGrade();
}

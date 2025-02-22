package com.example.wms.infrastructure.mapper;

import com.example.wms.order.application.domain.OrderProduct;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderProductMapper {
    void batchInsert(@Param("products") List<OrderProduct> orderProducts);

    OrderProduct findByProductId(@Param("productId") Long productId);

    void save(@Param("orderProduct") OrderProduct orderProduct);

    OrderProduct findByOrderId(@Param("orderId") Long orderId, @Param("productId") Long productId);

}

package com.example.wms.infrastructure.mapper;

import com.example.wms.order.adapter.in.dto.OrderResponseDto;
import com.example.wms.order.adapter.in.dto.ProductListDto;
import com.example.wms.order.application.domain.Order;
import com.example.wms.outbound.adapter.in.dto.OutboundPickingResponseDto;
import org.apache.ibatis.annotations.*;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface OrderMapper {

    List<Order> findAll(@Param("limit") int limit, @Param("offset") int offset);

    Order findOrderById(@Param("orderId") Long orderId);

    List<Order> findByIsApproved(@Param("isApproved") boolean isApproved);

    List<Order> findByIsDelayed(@Param("isDelayed") boolean isDelayed);

    // 승인된(isApproved = true) & 지연되지 않은(isDelayed = false) 주문 조회
    List<Order> findApprovedAndNotDelayed();

    String getLastOrderNumber();

    void createOrder(Order order);

    // 발주 등록하기
    void registerOrder(Order order);

    String findMaxOutboundOrderNumber();

    // 발주 삭제하기
    void deleteOrder(@Param("orderId") Long orderId);

    void deleteOrderProduct(@Param("orderId") Long orderId);

    @Transactional
    default void deleteOrderOrderProduct(@Param("orderId") Long orderId){
        deleteOrderProduct(orderId);

        deleteOrder(orderId);
    }

    // 발주 수정하기
    void upDateOrderProducts(@Param("orderId") Long orderId, @Param("productList") List<ProductListDto> productList);

    List<OrderResponseDto> findOrderFilteringWithPageNation(
            @Param("orderNumber") String orderNumber,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("pageable") Pageable pageable);

    Integer countAllOrder(
            @Param("orderNumber") String orderNumber,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    List<ProductListDto> findProductListByOrderId(Long orderId);


    void upDateOrderApprove(@Param("orderId") Long orderId);

    List<OrderResponseDto> findOrderSupplierFilteringWithPageNation(
            @Param("supplierId") Long supplierId,
            @Param("orderNumber") String orderNumber,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("pageable") Pageable pageable);

    Integer countAllOrderSupplier(
            @Param("supplierId") Long supplierId,
            @Param("orderNumber") String orderNumber,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}

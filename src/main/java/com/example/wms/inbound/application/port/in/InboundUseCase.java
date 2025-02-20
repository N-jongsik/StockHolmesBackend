package com.example.wms.inbound.application.port.in;

import com.example.wms.inbound.adapter.in.dto.request.*;
import com.example.wms.inbound.adapter.in.dto.response.*;
import com.example.wms.order.application.domain.Order;
import com.example.wms.order.application.domain.OrderProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface InboundUseCase {
    Long createInboundPlan(InboundReqDto inboundReqDto);
    Page<InboundResDto> getInboundPlans(Pageable pageable);
    Page<InboundResDto> getFilteredInboundPlans(String inboundScheduleNumber, LocalDate startDate, LocalDate endDate, Pageable pageable);
    List<InboundProductDto> getAllInboundProductList(OrderProduct orderProduct);
    void createInboundSchedule(Order order);
    void deleteInboundPlan(Long inboundId);
    void createInboundCheck(Long inboundId, InboundCheckReqDto inboundCheckReqDto);
    Page<InboundResDto> getFilteredInboundCheck(String inboundCheckNumber, LocalDate startDate, LocalDate endDate, Pageable pageable);
    void updateInboundCheck(Long inboundId, InboundCheckUpdateReqDto updateReqDto);
    void deleteInboundCheck(Long inboundId);
    InboundWorkerCheckResDto createInboundCheckByWorker(List<InboundCheckWorkerReqDto> workerCheckRequests);
    Page<InboundPutAwayResDto> getFilteredPutAway(String inboundPutAwayNumber, LocalDate startDate, LocalDate endDate, Pageable pageable);
    void putAway(Long inboundId);
    Page<ProductInboundResDto> getAllInboundByProductWithPagination(LocalDate startDate, LocalDate endDate, Pageable pageable);
    Page<SupplierInboundResDto> getAllInboundBySupplierWithPagination(LocalDate startDate, LocalDate endDate, Pageable pageable);
    Page<InboundProgressResDto> getAllInboundProgressWithPagination(LocalDate startDate, LocalDate endDate, Pageable pageable);
}

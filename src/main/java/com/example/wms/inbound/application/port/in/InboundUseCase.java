package com.example.wms.inbound.application.port.in;

import com.example.wms.inbound.adapter.in.dto.request.InboundCheckReqDto;
import com.example.wms.inbound.adapter.in.dto.request.InboundReqDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundProductDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundResDto;
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
    void createInboundCheck(InboundCheckReqDto inboundCheckReqDto);
    Page<InboundResDto> getFilteredInboundCheck(String inboundCheckNumber, LocalDate startDate, LocalDate endDate, Pageable pageable);
}

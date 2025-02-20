package com.example.wms.inbound.adapter.out;

import com.example.wms.inbound.adapter.in.dto.response.*;
import com.example.wms.inbound.application.port.out.InboundRetrievalPort;
import com.example.wms.infrastructure.mapper.InboundCheckMapper;
import com.example.wms.infrastructure.mapper.InboundPlanMapper;
import com.example.wms.infrastructure.mapper.InboundPutAwayMapper;
import com.example.wms.infrastructure.mapper.InboundRetrievalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InboundRetrievalAdapter implements InboundRetrievalPort {

    private final InboundRetrievalMapper inboundRetrievalMapper;
    private final InboundCheckMapper inboundCheckMapper;
    private final InboundPlanMapper inboundPlanMapper;
    private final InboundPutAwayMapper inboundPutAwayMapper;

    @Override
    public List<InboundProductDto> findInboundProductListByOrderId(Long orderId) {
        return inboundRetrievalMapper.findInboundProductListByOrderId(orderId);
    }

    @Override
    public List<InboundAllProductDto> findInboundProductListWithPagination(Pageable pageable) {
        return inboundRetrievalMapper.findInboundProductListWithPagination(pageable);
    }

    @Override
    public List<InboundAllProductDto> findInboundFilteringWithPagination(String inboundScheduleNumber, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return inboundPlanMapper.findInboundPlanFilteringWithPagination(inboundScheduleNumber, startDate, endDate, pageable);
    }

    @Override
    public List<InboundAllProductDto> findInboundCheckFilteringWithPagination(String inboundCheckNumber, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return inboundCheckMapper.findInboundCheckFilteringWithPagination(inboundCheckNumber, startDate, endDate, pageable);
    }

    @Override
    public Integer countAllInboundPlan() {
        return inboundRetrievalMapper.countAllInboundPlan();
    }

    @Override
    public Integer countFilteredInboundPlan(String inboundScheduleNumber, LocalDate startDate, LocalDate endDate) {
        return inboundRetrievalMapper.countAllInboundPlanFiltering(inboundScheduleNumber, startDate, endDate);
    }

    @Override
    public Integer countFilteredInboundCheck(String inboundCheckNumber, LocalDate startDate, LocalDate endDate) {
        return inboundRetrievalMapper.countAllInboundCheckFiltering(inboundCheckNumber, startDate, endDate);
    }

    @Override
    public List<InboundPutAwayAllProductDto> findFilteredInboundPutAway(String inboundPutAwayNumber, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return inboundPutAwayMapper.findInboundPutAwayFilteringWithPagination(inboundPutAwayNumber, startDate, endDate, pageable);
    }

    @Override
    public Integer countFilteredPutAway(String inboundPutAwayNumber, LocalDate startDate, LocalDate endDate) {
        return inboundRetrievalMapper.countFilteredPutAway(inboundPutAwayNumber, startDate, endDate);
    }

    @Override
    public List<ProductInboundResDto> findAllInboundByProductWithPagination(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return inboundRetrievalMapper.findAllInboundByProductWithPagination(startDate, endDate, pageable);
    }

    @Override
    public List<SupplierInboundResDto> findAllInboundBySupplierWithPagination(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return inboundRetrievalMapper.findAllInboundBySupplierWithPagination(startDate, endDate, pageable);
    }

    @Override
    public List<InboundProgressDetailDto> findAllInboundProgressWithPagination(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return inboundRetrievalMapper.findAllInboundProgressWithPagination(startDate, endDate, pageable);
    }


}

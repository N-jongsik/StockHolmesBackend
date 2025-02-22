package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.response.InboundAllProductDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundProductDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundResDto;
import com.example.wms.inbound.application.domain.Inbound;
import com.example.wms.inbound.application.port.in.GetInboundPlanUseCase;
import com.example.wms.inbound.application.port.out.GetInboundPlanPort;
import com.example.wms.infrastructure.pagination.util.PageableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetInboundPlanService implements GetInboundPlanUseCase {

    private final GetInboundPlanPort getInboundPlanPort;

    private List<InboundResDto> convertToInboundResDto(List<InboundAllProductDto> inboundDtoList) {
        if (inboundDtoList.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, InboundResDto> inboundMap = new LinkedHashMap<>();

        for (InboundAllProductDto dto : inboundDtoList) {

            inboundMap.putIfAbsent(dto.getInboundId(),
                    InboundResDto.builder()
                            .inboundId(dto.getInboundId())
                            .inboundStatus(dto.getInboundStatus())
                            .createdAt(dto.getCreatedAt())
                            .scheduleNumber(dto.getScheduleNumber())
                            .scheduleDate(dto.getScheduleDate())
                            .checkNumber(dto.getInboundCheckNumber())
                            .checkDate(dto.getCheckDate())
                            .orderId(dto.getOrderId())
                            .orderNumber(dto.getOrderNumber())
                            .orderDate(dto.getOrderDate())
                            .supplierId(dto.getSupplierId())
                            .supplierName(dto.getSupplierName())
                            .productList(new ArrayList<>())
                            .build()
            );

            InboundResDto existingResDto = inboundMap.get(dto.getInboundId());

            if (dto.getProductList() != null && !dto.getProductList().isEmpty()) {
                List<InboundProductDto> convertedProducts = dto.getProductList().stream()
                        .map(product -> InboundProductDto.builder()
                                .productId(product.getProductId())
                                .productCode(product.getProductCode())
                                .productName(product.getProductName())
                                .productCount(product.getProductCount())
                                .stockLotCount(product.getStockLotCount())
                                .defectiveCount(product.getDefectiveCount())
                                .build())
                        .collect(Collectors.toList());

                existingResDto.getProductList().addAll(convertedProducts);
            }

        }

        return new ArrayList<>(inboundMap.values());
    }

    @Override
    public Page<InboundResDto> getFilteredInboundPlans(String inboundScheduleNumber, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Pageable safePageable = PageableUtils.convertToSafePageableStrict(pageable, Inbound.class);
        List<InboundAllProductDto> inboundAllProductDtoList = getInboundPlanPort.findInboundFilteringWithPagination(inboundScheduleNumber, startDate, endDate, safePageable);

        Integer count = getInboundPlanPort.countFilteredInboundPlan(inboundScheduleNumber, startDate, endDate);

        List<InboundResDto> inboundResDtoList = convertToInboundResDto(inboundAllProductDtoList);

        return new PageImpl<>(inboundResDtoList,pageable,count);
    }
}

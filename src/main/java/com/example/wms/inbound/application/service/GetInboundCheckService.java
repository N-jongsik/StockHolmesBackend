package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.response.InboundAllProductDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundProductDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundResDto;
import com.example.wms.inbound.adapter.in.dto.response.LotResDto;
import com.example.wms.inbound.application.domain.Inbound;
import com.example.wms.inbound.application.port.in.GetInboundCheckUseCase;
import com.example.wms.inbound.application.port.out.InboundRetrievalPort;
import com.example.wms.infrastructure.pagination.util.PageableUtils;
import com.example.wms.product.application.domain.Lot;
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
public class GetInboundCheckService implements GetInboundCheckUseCase {

    private final InboundRetrievalPort inboundRetrievalPort;

    @Override
    public Page<InboundResDto> getFilteredInboundCheck(String inboundCheckNumber, LocalDate startDate, LocalDate endDate, Pageable pageable) {

        Pageable safePageable = PageableUtils.convertToSafePageableStrict(pageable, Inbound.class);
        List<InboundAllProductDto> inboundAllProductDtoList = inboundRetrievalPort.findInboundCheckFilteringWithPagination(inboundCheckNumber, startDate, endDate, safePageable);

        Integer count = inboundRetrievalPort.countFilteredInboundCheck(inboundCheckNumber, startDate, endDate);
        List<InboundResDto> inboundResDtoList = convertToInboundResDto(inboundAllProductDtoList);

        return new PageImpl<>(inboundResDtoList, pageable, count);
    }

    private List<InboundResDto> convertToInboundResDto(List<InboundAllProductDto> inboundDtoList) {
        if (inboundDtoList.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, InboundResDto> inboundMap = new LinkedHashMap<>();
        Map<Long, LotResDto> lotMap = new LinkedHashMap<>();

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
                            .lotList(new ArrayList<>())
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


            if (dto.getLotList() != null && !dto.getLotList().isEmpty()) {
                List<LotResDto> convertedLots = dto.getLotList().stream()
                        .map(lot -> LotResDto.builder()
                                .lotId(lot.getLotId())
                                .lotNumber(lot.getLotNumber())
                                .productId(lot.getProductId())
                                .productCode(lot.getProductCode())
                                .productName(lot.getProductName())
                                .productCount(lot.getProductCount())
                                .locationBinCode(lot.getLocationBinCode())
                                .build())
                        .collect(Collectors.toList());
                existingResDto.getLotList().addAll(convertedLots);
            }

        }

        return new ArrayList<>(inboundMap.values());
    }


}

package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.response.*;
import com.example.wms.inbound.application.domain.Inbound;
import com.example.wms.inbound.application.port.in.GetInboundPutAwayUseCase;
import com.example.wms.inbound.application.port.out.InboundRetrievalPort;
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
public class GetInboundPutAwayService implements GetInboundPutAwayUseCase {

    private final InboundRetrievalPort inboundRetrievalPort;

    @Override
    public Page<InboundPutAwayResDto> getFilteredPutAway(String inboundPutAwayNumber, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Pageable safePageable = PageableUtils.convertToSafePageableStrict(pageable, Inbound.class);

        List<InboundPutAwayAllProductDto> inboundPutAwayList = inboundRetrievalPort.findFilteredInboundPutAway(inboundPutAwayNumber, startDate, endDate, safePageable);

        Integer count = inboundRetrievalPort.countFilteredPutAway(inboundPutAwayNumber, startDate, endDate);

        List<InboundPutAwayResDto> inboundPutAwayResDtoList = convertToInboundPutAwayResDto(inboundPutAwayList);

        return new PageImpl<>(inboundPutAwayResDtoList, safePageable, count);
    }

    private List<InboundPutAwayResDto> convertToInboundPutAwayResDto(List<InboundPutAwayAllProductDto> inboundPutAwayDtoList) {
        if (inboundPutAwayDtoList.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, InboundPutAwayResDto> inboundPutAwayMap = new LinkedHashMap<>();

        for (InboundPutAwayAllProductDto dto : inboundPutAwayDtoList) {
            inboundPutAwayMap.putIfAbsent(dto.getInboundId(),
                    InboundPutAwayResDto.builder()
                            .inboundId(dto.getInboundId())
                            .inboundStatus(dto.getInboundStatus())
                            .createdAt(dto.getCreatedAt())
                            .scheduleNumber(dto.getScheduleNumber())
                            .inboundCheckNumber(dto.getInboundCheckNumber())
                            .putAwayNumber(dto.getPutAwayNumber())
                            .putAwayDate(dto.getPutAwayDate())
                            .orderId(dto.getOrderId())
                            .orderNumber(dto.getOrderNumber())
                            .orderDate(dto.getOrderDate())
                            .supplierId(dto.getSupplierId())
                            .supplierName(dto.getSupplierName())
                            .lotList(dto.getLotList())
                            .build()
            );

            InboundPutAwayResDto existingPutAwayResDto = inboundPutAwayMap.get(dto.getInboundId());

            if (dto.getLotList() != null && !dto.getLotList().isEmpty()) {
                List<LotResDto> convertedProducts = dto.getLotList().stream()
                        .map(product -> LotResDto.builder()
                                .lotId(product.getLotId())
                                .lotNumber(product.getLotNumber())
                                .productId(product.getProductId())
                                .productCode(product.getProductCode())
                                .productName(product.getProductName())
                                .productCount(product.getProductCount())
                                .locationBinCode(product.getLocationBinCode())
                                .build())
                        .collect(Collectors.toList());
                existingPutAwayResDto.getLotList().addAll(convertedProducts);
            }
        }
        return new ArrayList<>(inboundPutAwayMap.values());
    }




}

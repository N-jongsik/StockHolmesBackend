package com.example.wms.outbound.application.service;

import com.example.wms.infrastructure.pagination.util.PageableUtils;
import com.example.wms.outbound.adapter.in.dto.OutboundPackingResponseDto;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.in.GetOutboundPackingUseCase;
import com.example.wms.outbound.application.port.out.GetOutboundPackingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetOutboundPackingService implements GetOutboundPackingUseCase {

    private final GetOutboundPackingPort getOutboundPackingPort;

    @Override
    @Transactional
    public Page<OutboundPackingResponseDto> getFilteredOutboundPackings(String outboundPackingNumber, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Pageable safePageale = PageableUtils.convertToSafePageableStrict(pageable, OutboundPackingResponseDto.class);
        List<OutboundPackingResponseDto> outboundList = getOutboundPackingPort.findOutboundPackingFilteringWithPageNation(outboundPackingNumber, startDate, endDate, safePageale);
        Integer count = getOutboundPackingPort.countPacking(outboundPackingNumber, startDate, endDate);
        return new PageImpl<>(outboundList, pageable, count);
    }

    private List<OutboundPackingResponseDto> covertToDtoList(List<Outbound> outboundList) {
        return outboundList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private OutboundPackingResponseDto convertToDto(Outbound outbound) {
        List<ProductInfoDto> productList = getOutboundPackingPort.findProductInfoByOutboundPlanId(outbound.getOutboundPlanId())
                .stream()
                .map(p->ProductInfoDto.builder()
                        .productId(p.getProductId())
                        .productCode(p.getProductCode())
                        .productName(p.getProductName())
                        .productCount(p.getProductCount())
                        .build())
                .collect(Collectors.toList());

        OutboundPlan outboundPlan = getOutboundPackingPort.findOutboundPlanByOutboundPlanId(outbound.getOutboundPlanId());

        String process = "출고지시";

        if(outbound.getOutboundPickingNumber() != null && outbound.getOutboundPackingNumber() != null && outbound.getOutboundLoadingNumber() != null) {
            process = "출하상차 및 확정";
        } else if(outbound.getOutboundPickingNumber() != null && outbound.getOutboundPackingNumber() != null) {
            process = "출고패킹";
        } else if(outbound.getOutboundPickingNumber() != null) {
            process = "출고피킹";
        }

        return OutboundPackingResponseDto.builder()
                .outboundId(outbound.getOutboundId())
                .outboundPlanId(outbound.getOutboundPlanId())
                .process(process) //outbound테이블 보고 수정하기
                .outboundScheduleNumber(outboundPlan.getOutboundScheduleNumber())
                .outboundAssignNumber(outbound.getOutboundAssignNumber())
                .outboundPickingNumber(outbound.getOutboundPickingNumber())
                .outboundPackingNumber(outbound.getOutboundPackingNumber())
                .outboundPackingDate(outbound.getOutboundPackingDate())
                .productionPlanNumber(outboundPlan.getProductionPlanNumber())
                .planDate(outboundPlan.getPlanDate())
                .productList(productList)
                .build();
    }
}

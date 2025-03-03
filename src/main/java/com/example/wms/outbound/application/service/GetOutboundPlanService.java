package com.example.wms.outbound.application.service;

import com.example.wms.infrastructure.pagination.util.PageableUtils;
import com.example.wms.outbound.adapter.in.dto.OutboundPlanResponseDto;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.in.GetOutboundPlanUseCase;
import com.example.wms.outbound.application.port.out.GetOutboundPlanPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetOutboundPlanService implements GetOutboundPlanUseCase {

    private final GetOutboundPlanPort getOutboundPlanPort;

    @Override
    @Transactional(readOnly = true)
    public Page<OutboundPlanResponseDto> getFilteredOutboundPlans(String outboundScheduleNumber, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Map<String, String> fieldMapping = new HashMap<>();
        fieldMapping.put("process", "status");
        Pageable safePageable = PageableUtils.convertToSafePageableStrict(pageable, OutboundPlanResponseDto.class, fieldMapping);
        List<OutboundPlanResponseDto> outboundPlanList = getOutboundPlanPort.findOutboundPlanFilteringWithPageNation(outboundScheduleNumber, startDate, endDate, safePageable);
        Integer count = getOutboundPlanPort.countFilteredOutboundPlan(outboundScheduleNumber, startDate, endDate);
        return new PageImpl<>(outboundPlanList, pageable, count);
    }

    private List<OutboundPlanResponseDto> convertToDtoList(List<OutboundPlan> outboundPlanList) {
        return outboundPlanList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    private OutboundPlanResponseDto convertToDto(OutboundPlan outboundPlan) {
        List<ProductInfoDto> productList = getOutboundPlanPort.findProductInfoByOutboundPlanId(outboundPlan.getOutboundPlanId())
                .stream()
                .map(p -> ProductInfoDto.builder()
                        .productId(p.getProductId())
                        .productCode(p.getProductCode())
                        .productName(p.getProductName())
                        .productCount(p.getProductCount())
                        .build())
                .collect(Collectors.toList());

        // outboundPlanId로 outbound찾기 -> 없으면 출고예정 있으면 출고 지시,피킹,패킹,로딩 나누기
        Outbound outbound = getOutboundPlanPort.findOutboundByOutboundPlanId(outboundPlan.getOutboundPlanId()).orElse(null);

        String process = "진행중";

        if(outbound == null) {
            process = "출고예정";
        } else if(outbound.getOutboundPickingNumber() != null && outbound.getOutboundPackingNumber() != null && outbound.getOutboundLoadingNumber() != null) {
            process = "출하상차 및 확정";
        } else if(outbound.getOutboundPickingNumber() != null && outbound.getOutboundPackingNumber() != null) {
            process = "출고패킹";
        } else if(outbound.getOutboundPickingNumber() != null) {
            process = "출고피킹";
        }

        return OutboundPlanResponseDto.builder()
                .outboundPlanId(outboundPlan.getOutboundPlanId())
                .process(process)  // outbound테이블에서 assign있으면, picking있으면, packing있으면 이걸로 다시 나누기
                .outboundScheduleNumber(outboundPlan.getOutboundScheduleNumber())
                .outboundScheduleDate(outboundPlan.getOutboundScheduleDate())
                .productionPlanNumber(outboundPlan.getProductionPlanNumber())
                .planDate(outboundPlan.getPlanDate())
                .productList(productList)
                .build();
    }
}

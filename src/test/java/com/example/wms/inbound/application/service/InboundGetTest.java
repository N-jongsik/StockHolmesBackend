package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.response.InboundAllProductDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundProductDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundResDto;
import com.example.wms.inbound.application.port.out.InboundRetrievalPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class InboundGetTest {

    private static final Logger logger = LoggerFactory.getLogger(InboundGetTest.class);

    @Mock
    InboundRetrievalPort inboundRetrievalPort;

    @InjectMocks
    GetInboundPlanService getInboundPlanService;

    private List<InboundAllProductDto> mockInboundPlanProductList;

    @BeforeEach
    void setUp() {
        mockInboundPlanProductList = Arrays.asList(
                InboundAllProductDto.builder()
                        .inboundId(1L)
                        .inboundStatus("입고중")
                        .createdAt(LocalDate.now())
                        .scheduleNumber("Schedule1")
                        .scheduleDate(LocalDate.now())
                        .checkDate(LocalDate.now())
                        .inboundCheckNumber("Check1")
                        .orderId(1L)
                        .orderNumber("or1")
                        .orderDate(LocalDate.now())
                        .supplierId(1L)
                        .supplierName("Supplier1")
                        .productList(Arrays.asList(
                                InboundProductDto.builder()
                                        .productId(101L)
                                        .productCode("P001")
                                        .productName("Product1")
                                        .productCount(10L)
                                        .stockLotCount(5L)
                                        .defectiveCount(1L)
                                        .build(),
                                InboundProductDto.builder()
                                        .productId(102L)
                                        .productCode("P002")
                                        .productName("Product2")
                                        .productCount(15L)
                                        .stockLotCount(7L)
                                        .defectiveCount(2L)
                                        .build()
                        ))
                        .build(),

                InboundAllProductDto.builder()
                        .inboundId(2L)
                        .inboundStatus("입고완료")
                        .createdAt(LocalDate.now())
                        .scheduleNumber("Schedule2")
                        .scheduleDate(LocalDate.now())
                        .checkDate(LocalDate.now())
                        .inboundCheckNumber("Check2")
                        .orderId(2L)
                        .orderNumber("or2")
                        .orderDate(LocalDate.now())
                        .supplierId(2L)
                        .supplierName("Supplier2")
                        .productList(Arrays.asList(
                                InboundProductDto.builder()
                                        .productId(103L)
                                        .productCode("P003")
                                        .productName("Product3")
                                        .productCount(20L)
                                        .stockLotCount(10L)
                                        .defectiveCount(3L)
                                        .build(),
                                InboundProductDto.builder()
                                        .productId(104L)
                                        .productCode("P004")
                                        .productName("Product4")
                                        .productCount(25L)
                                        .stockLotCount(12L)
                                        .defectiveCount(4L)
                                        .build()
                        ))
                        .build()
        );
    }


    @Test
    @DisplayName("입하 예정 목록을 필터링하여 조회합니다.")
    void testGetFilteredInboundPlans() {

        // given
        String inboundScheduleNumber = "Schedule1";
        LocalDate startDate = LocalDate.of(2025, 2, 1);
        LocalDate endDate = LocalDate.of(2025, 2, 10);
        Pageable pageable = PageRequest.of(0, 10);

        when(inboundRetrievalPort.findInboundFilteringWithPagination(eq(inboundScheduleNumber), eq(startDate), eq(endDate), any(Pageable.class)))
                .thenReturn(mockInboundPlanProductList);
        when(inboundRetrievalPort.countFilteredInboundPlan(eq(inboundScheduleNumber), eq(startDate), eq(endDate)))
                .thenReturn(mockInboundPlanProductList.size());

        // when
        Page<InboundResDto> result = getInboundPlanService.getFilteredInboundPlans(inboundScheduleNumber, startDate, endDate, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(mockInboundPlanProductList.size());
        assertThat(result.getTotalElements()).isEqualTo(mockInboundPlanProductList.size());

        verify(inboundRetrievalPort, times(1)).findInboundFilteringWithPagination(eq(inboundScheduleNumber), eq(startDate), eq(endDate), any(Pageable.class));
        verify(inboundRetrievalPort, times(1)).countFilteredInboundPlan(eq(inboundScheduleNumber), eq(startDate), eq(endDate));
    }


}
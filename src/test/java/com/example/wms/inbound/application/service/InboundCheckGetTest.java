package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.response.InboundAllProductDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundResDto;
import com.example.wms.inbound.application.port.out.InboundRetrievalPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InboundCheckGetTest {

    @Mock
    InboundRetrievalPort inboundRetrievalPort;

    @InjectMocks
    InboundService inboundService;

    private List<InboundAllProductDto> mockInboundCheckProductList;

    @BeforeEach
    void setUp() {
        mockInboundCheckProductList = Arrays.asList(
                new InboundAllProductDto(1L,"입하검사완료",LocalDate.now(),"IC202502010001",LocalDate.now(),1L,"OR202502010001",LocalDateTime.now(),1L,"tire company",1L,"a123","tire1",30,3),
                new InboundAllProductDto(2L,"입하검사완료",LocalDate.now(),"IC202502010002",LocalDate.now(),1L,"OR202502010001",LocalDateTime.now(),1L,"tire company",1L,"a123","tire2",50,5)
        );
    }

    @Test
    @DisplayName("입하 검사 전체 목록을 정상적으로 조회하는 경우를 테스트합니다.")
    void testGetInboundCheck() {
        Pageable pageable = PageRequest.of(0,10);
        when(inboundRetrievalPort.findInboundProductListWithPagination(any(Pageable.class)))
                .thenReturn(mockInboundCheckProductList);

        when(inboundRetrievalPort.countAllInboundPlan()).thenReturn(mockInboundCheckProductList.size());

        Page<InboundResDto> result = inboundService.getInboundPlans(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(mockInboundCheckProductList.size());
        assertThat(result.getTotalElements()).isEqualTo(mockInboundCheckProductList.size());

        verify(inboundRetrievalPort, times(1)).findInboundProductListWithPagination(any(Pageable.class));
        verify(inboundRetrievalPort, times(1)).countAllInboundPlan();
    }

    @Test
    @DisplayName("입하 검사 전체 목록을 입하번호 및 기간별로 조회하는 경우를 테스트합니다.")
    void testGetFilteredInboundChecks() {

        // given
        String inboundCheckNumber = "IC202502150001";

        LocalDate startDate = LocalDate.of(2025,2,15);
        LocalDate endDate = LocalDate.of(2025,2,16);
        Pageable pageable = PageRequest.of(0,10);

        when(inboundRetrievalPort.findInboundFilteringWithPagination(eq(inboundCheckNumber), eq(startDate),eq(endDate),any(Pageable.class)))
                .thenReturn(mockInboundCheckProductList);

        when(inboundRetrievalPort.countFilteredInboundPlan(eq(inboundCheckNumber),eq(startDate),eq(endDate)))
                .thenReturn(mockInboundCheckProductList.size());

        // when
        Page<InboundResDto> result = inboundService.getFilteredInboundPlans(inboundCheckNumber, startDate, endDate, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(mockInboundCheckProductList.size());
        assertThat(result.getTotalElements()).isEqualTo(mockInboundCheckProductList.size());

        verify(inboundRetrievalPort, times(1)).findInboundFilteringWithPagination(eq(inboundCheckNumber), eq(startDate), eq(endDate), any(Pageable.class));
        verify(inboundRetrievalPort, times(1)).countFilteredInboundPlan(eq(inboundCheckNumber), eq(startDate), eq(endDate));
    }

}

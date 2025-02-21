package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.response.InboundDetailDto;
import com.example.wms.inbound.adapter.in.dto.response.ProductInboundResDto;
import com.example.wms.inbound.application.port.out.InboundRetrievalPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
public class InboundProductPaginationTest {

    @Mock
    InboundRetrievalPort inboundRetrievalPort;

    @InjectMocks
    InboundService inboundService;

    private List<ProductInboundResDto> mockProductInboundList;

    @BeforeEach
    void setUp() {
        List<InboundDetailDto> inboundDetails1 = Arrays.asList(
                 new InboundDetailDto(1L,"IS202502010001", "IC202502150001", "PA202502150001", "2025-02-15", "2025-02-16", "2025-02-17"),
                new InboundDetailDto(1L,"IS202502010002", "IC202502150002", "PA202502150002", "2025-02-15", "2025-02-16", "2025-02-17")
        );

        List<InboundDetailDto> inboundDetails2 = Arrays.asList(
                new InboundDetailDto(1L,"IS202502010001", "IC202502160001", "PA202502160001", "2025-02-16", "2025-02-17", "2025-02-18")
        );

        mockProductInboundList = Arrays.asList(
                new ProductInboundResDto(1001L, "A123", "Engine Part", 50, 5, inboundDetails1),
                new ProductInboundResDto(1002L, "B456", "Brake Pad", 30, 3, inboundDetails2)
        );
    }

    @Test
    @DisplayName("품목 별 입고조회를 테스트합니다.")
    void testGetAllInboundByProductWithPagination() {

        // given
        LocalDate startDate = LocalDate.of(2025,2,15);
        LocalDate endDate = LocalDate.of(2025,2,18);
        Pageable pageable = PageRequest.of(0,10);

        Page<ProductInboundResDto> mockPage = new PageImpl<>(mockProductInboundList, pageable, mockProductInboundList.size());

        when(inboundRetrievalPort.findAllInboundByProductWithPagination(eq(startDate), eq(endDate), any(Pageable.class)))
                .thenReturn(mockProductInboundList);

        // when
        Page<ProductInboundResDto> result = inboundService.getAllInboundByProductWithPagination(startDate, endDate, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(mockProductInboundList.size());
        assertThat(result.getContent().get(0).getProductId()).isEqualTo(1001L);
        assertThat(result.getContent().get(1).getProductId()).isEqualTo(1002L);
        assertThat(result.getContent().get(0).getInboundList()).hasSize(2);
        assertThat(result.getContent().get(1).getInboundList()).hasSize(1);

        verify(inboundRetrievalPort, times(1)).findAllInboundByProductWithPagination(eq(startDate), eq(endDate), any(Pageable.class));

    }

}

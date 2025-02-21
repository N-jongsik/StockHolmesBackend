package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.response.SupplierInboundDetailDto;
import com.example.wms.inbound.adapter.in.dto.response.SupplierInboundResDto;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
public class InboundSupplierPaginationTest {

    @Mock
    InboundRetrievalPort inboundRetrievalPort;

    @InjectMocks
    InboundService inboundService;

    private List<SupplierInboundResDto> mockSupplierInboundList;

    @BeforeEach
    void setUp() {
        List<SupplierInboundDetailDto> inboundDetails = Arrays.asList(
                new SupplierInboundDetailDto(101L, "IS202502150001", "IC202502150001", "PA202502150001", "2025-02-15", "2025-02-16", "2025-02-17")
        );

        mockSupplierInboundList = Arrays.asList(
                new SupplierInboundResDto(5001L, "Hyundai Supplier", 100, 10, inboundDetails)
        );
    }

    @Test
    @DisplayName("납품업체별 입고 현황 조회를 테스트합니다.")
    void testGetAllInboundBySupplierWithPagination() {
        LocalDate startDate = LocalDate.of(2025,2,15);
        LocalDate endDate = LocalDate.of(2025,2,18);
        Pageable pageable = PageRequest.of(0,10);

        when(inboundRetrievalPort.findAllInboundBySupplierWithPagination(startDate, endDate, pageable))
                .thenReturn(mockSupplierInboundList);

        Page<SupplierInboundResDto> result = inboundService.getAllInboundBySupplierWithPagination(startDate, endDate, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(inboundRetrievalPort, times(1)).findAllInboundBySupplierWithPagination(startDate, endDate, pageable);
    }
}

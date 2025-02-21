package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.response.InboundProgressDetailDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundProgressResDto;
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
public class InboundProgressPaginationTest {

    @Mock
    InboundRetrievalPort inboundRetrievalPort;

    @InjectMocks
    InboundService inboundService;

    private List<InboundProgressDetailDto> mockInboundList;

    @BeforeEach
    void setUp() {
        mockInboundList = Arrays.asList(
                new InboundProgressDetailDto(101L, "SC202502150001", null, null, "2025-02-15", null, null, "OR12345", "2025-02-14", "Hyundai Supplier"),
                new InboundProgressDetailDto(102L, "SC202502150002", "IC202502150002", null, "2025-02-15", "2025-02-16", null, "OR12346", "2025-02-14", "Kia Supplier")
        );
    }

    @Test
    @DisplayName("모든 입고진행별 입고 현황을 조회하는 테스트입니다.")
    void testGetAllInboundProgressWithPagination() {
        LocalDate startDate = LocalDate.of(2025, 2, 15);
        LocalDate endDate = LocalDate.of(2025, 2, 18);
        Pageable pageable = PageRequest.of(0, 10);

        when(inboundRetrievalPort.findAllInboundProgressWithPagination(startDate, endDate, pageable))
                .thenReturn(mockInboundList);

        Page<InboundProgressResDto> result = inboundService.getAllInboundProgressWithPagination(startDate, endDate, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(inboundRetrievalPort, times(1)).findAllInboundProgressWithPagination(startDate, endDate, pageable);
    }


}

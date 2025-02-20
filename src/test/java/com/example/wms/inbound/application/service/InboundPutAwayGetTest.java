package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.response.InboundProductDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundPutAwayResDto;
import com.example.wms.inbound.adapter.in.dto.response.LotResDto;
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

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InboundPutAwayGetTest {

    @Mock
    InboundRetrievalPort inboundRetrievalPort;

    @InjectMocks
    InboundService inboundService;

    private List<InboundPutAwayResDto> mockInboundPutAwayList;

    @BeforeEach
    void setUp() {
        List<LotResDto> productList = Arrays.asList(
                new LotResDto(1001L, 1L, "A123", "Engine Part", 5L,"A-01-01-03"),
                new LotResDto(1002L, 2L,"B456", "Brake Pad", 30L, "A-01-01-03")
        );

        mockInboundPutAwayList = Arrays.asList(
                new InboundPutAwayResDto(
                        1L, "입고적치완료", "2025-02-15",
                        "SC202502150001", "IC202502150001", "PA202502150001", "2025-02-16",
                        1L, "OR202502150001", "2025-02-10", 1L, "Hyundai Supplier",
                        productList
                )
        );
    }

    @Test
    @DisplayName("입고 적치 목록 조회를 테스트합니다.")
    void testGetAllInboundPutAway() {

        // given
        String inboundPutAwayNumber = "PA202502150001";

        LocalDate startDate = LocalDate.of(2025,2,15);
        LocalDate endDate = LocalDate.of(2025,2,16);
        Pageable pageable = PageRequest.of(0,10);

        when(inboundRetrievalPort.countFilteredPutAway(eq(inboundPutAwayNumber),eq(startDate),eq(endDate)))
                .thenReturn(mockInboundPutAwayList.size());

        // when
        Page<InboundPutAwayResDto> result = inboundService.getFilteredPutAway(inboundPutAwayNumber, startDate, endDate, pageable);

        // then
        assertThat(result).isNotNull();

        verify(inboundRetrievalPort, times(1)).findFilteredInboundPutAway(eq(inboundPutAwayNumber), eq(startDate), eq(endDate), any(Pageable.class));
        verify(inboundRetrievalPort, times(1)).countFilteredPutAway(eq(inboundPutAwayNumber), eq(startDate), eq(endDate));
        }
}

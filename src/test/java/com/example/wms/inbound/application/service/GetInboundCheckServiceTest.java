package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.response.InboundAllProductDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundResDto;
import com.example.wms.inbound.application.port.out.InboundRetrievalPort;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GetInboundCheckServiceTest {

    private GetInboundCheckService service;

    @Mock
    private InboundRetrievalPort inboundRetrievalPort;

    private Pageable pageable;
    private LocalDate startDate;
    private LocalDate endDate;
    private String inboundCheckNumber;

    @Before
    public void setUp() {
        service = new GetInboundCheckService(inboundRetrievalPort);
        pageable = PageRequest.of(0, 10);
        startDate = LocalDate.of(2025, 2, 1);
        endDate = LocalDate.of(2025, 2, 28);
        inboundCheckNumber = "CHK001";
    }

    @Test
    public void testGetFilteredInboundCheck_ReturnsData() {
        // InboundAllProductDto 모킹 및 필수 필드 스텁 설정
        InboundAllProductDto dto = mock(InboundAllProductDto.class);
        when(dto.getInboundId()).thenReturn(1L);
        when(dto.getInboundStatus()).thenReturn("STATUS");
        when(dto.getCreatedAt()).thenReturn(LocalDate.of(2025, 2, 15));
        when(dto.getScheduleNumber()).thenReturn("SCH001");
        when(dto.getScheduleDate()).thenReturn(LocalDate.of(2025, 2, 10));
        when(dto.getInboundCheckNumber()).thenReturn("CHK001");
        when(dto.getCheckDate()).thenReturn(LocalDate.of(2025, 2, 12));
        when(dto.getOrderId()).thenReturn(10L);
        when(dto.getOrderNumber()).thenReturn("ORD001");
        when(dto.getOrderDate()).thenReturn(LocalDate.of(2025, 2, 9));
        when(dto.getSupplierId()).thenReturn(100L);
        when(dto.getSupplierName()).thenReturn("Supplier A");
        when(dto.getProductList()).thenReturn(Collections.emptyList());
        when(dto.getLotList()).thenReturn(Collections.emptyList());

        when(inboundRetrievalPort.findInboundCheckFilteringWithPagination(
                eq(inboundCheckNumber), eq(startDate), eq(endDate), any(Pageable.class)))
                .thenReturn(Arrays.asList(dto));
        when(inboundRetrievalPort.countFilteredInboundCheck(eq(inboundCheckNumber), eq(startDate), eq(endDate)))
                .thenReturn(1);

        // When
        Page<InboundResDto> page = service.getFilteredInboundCheck(inboundCheckNumber, startDate, endDate, pageable);

        // Then
        verify(inboundRetrievalPort).findInboundCheckFilteringWithPagination(
                eq(inboundCheckNumber), eq(startDate), eq(endDate), any(Pageable.class));
        verify(inboundRetrievalPort).countFilteredInboundCheck(eq(inboundCheckNumber), eq(startDate), eq(endDate));

        assertNotNull(page);
        assertEquals(1, page.getTotalElements());

        InboundResDto resDto = page.getContent().get(0);
        assertEquals(Long.valueOf(1L), resDto.getInboundId());
        assertEquals("STATUS", resDto.getInboundStatus());
        assertEquals(LocalDate.of(2025, 2, 15), resDto.getCreatedAt());
        assertEquals("SCH001", resDto.getScheduleNumber());
        assertEquals(LocalDate.of(2025, 2, 10), resDto.getScheduleDate());
        assertEquals("CHK001", resDto.getCheckNumber());
        assertEquals(LocalDate.of(2025, 2, 12), resDto.getCheckDate());
        assertEquals(Long.valueOf(10L), resDto.getOrderId());
        assertEquals("ORD001", resDto.getOrderNumber());
        assertEquals(LocalDate.of(2025, 2, 9), resDto.getOrderDate());
        assertEquals(Long.valueOf(100L), resDto.getSupplierId());
        assertEquals("Supplier A", resDto.getSupplierName());
    }

    @Test
    public void testGetFilteredInboundCheck_ReturnsEmptyPage() {
        when(inboundRetrievalPort.findInboundCheckFilteringWithPagination(
                eq(inboundCheckNumber), eq(startDate), eq(endDate), any(Pageable.class)))
                .thenReturn(Collections.emptyList());
        when(inboundRetrievalPort.countFilteredInboundCheck(eq(inboundCheckNumber), eq(startDate), eq(endDate)))
                .thenReturn(0);

        Page<InboundResDto> page = service.getFilteredInboundCheck(inboundCheckNumber, startDate, endDate, pageable);

        verify(inboundRetrievalPort).findInboundCheckFilteringWithPagination(
                eq(inboundCheckNumber), eq(startDate), eq(endDate), any(Pageable.class));
        verify(inboundRetrievalPort).countFilteredInboundCheck(eq(inboundCheckNumber), eq(startDate), eq(endDate));

        assertNotNull(page);
        assertEquals(0, page.getTotalElements());
        assertTrue(page.getContent().isEmpty());
    }
}

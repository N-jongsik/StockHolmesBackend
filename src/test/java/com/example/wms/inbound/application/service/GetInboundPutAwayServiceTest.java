package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.response.InboundPutAwayAllProductDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundPutAwayResDto;
import com.example.wms.inbound.adapter.in.dto.response.LotResDto;
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
public class GetInboundPutAwayServiceTest {

    private GetInboundPutAwayService service;

    @Mock
    private InboundRetrievalPort inboundRetrievalPort;

    private Pageable pageable;
    private LocalDate startDate;
    private LocalDate endDate;
    private String inboundPutAwayNumber;

    @Before
    public void setUp() {
        service = new GetInboundPutAwayService(inboundRetrievalPort);
        pageable = PageRequest.of(0, 10);
        startDate = LocalDate.of(2025, 2, 1);
        endDate = LocalDate.of(2025, 2, 28);
        inboundPutAwayNumber = "PA001";
    }

    @Test
    public void testGetFilteredPutAway_WithData() {
        LocalDate createdAtDate = LocalDate.of(2025, 2, 15);
        LocalDate putAwayDate = LocalDate.of(2025, 2, 20);
        LocalDate orderDate = LocalDate.of(2025, 2, 10);

        InboundPutAwayAllProductDto dto = mock(InboundPutAwayAllProductDto.class);
        when(dto.getInboundId()).thenReturn(1L);
        when(dto.getInboundStatus()).thenReturn("STATUS");
        when(dto.getCreatedAt()).thenReturn(createdAtDate.toString());
        when(dto.getScheduleNumber()).thenReturn("SCH001");
        when(dto.getInboundCheckNumber()).thenReturn("CHK001");
        when(dto.getPutAwayNumber()).thenReturn("PA001");
        when(dto.getPutAwayDate()).thenReturn(putAwayDate.toString());
        when(dto.getOrderId()).thenReturn(10L);
        when(dto.getOrderNumber()).thenReturn("ORD001");
        when(dto.getOrderDate()).thenReturn(orderDate.toString());
        when(dto.getSupplierId()).thenReturn(100L);
        when(dto.getSupplierName()).thenReturn("SupplierA");

        LotResDto lot = mock(LotResDto.class);
        when(lot.getLotId()).thenReturn(200L);
        when(lot.getProductCode()).thenReturn("P001");
        when(dto.getLotList()).thenReturn(Arrays.asList(lot));

        when(inboundRetrievalPort.findFilteredInboundPutAway(eq(inboundPutAwayNumber), eq(startDate), eq(endDate), any(Pageable.class)))
                .thenReturn(Arrays.asList(dto));
        when(inboundRetrievalPort.countFilteredPutAway(eq(inboundPutAwayNumber), eq(startDate), eq(endDate)))
                .thenReturn(1);

        Page<InboundPutAwayResDto> page = service.getFilteredPutAway(inboundPutAwayNumber, startDate, endDate, pageable);

        verify(inboundRetrievalPort).findFilteredInboundPutAway(eq(inboundPutAwayNumber), eq(startDate), eq(endDate), any(Pageable.class));
        verify(inboundRetrievalPort).countFilteredPutAway(eq(inboundPutAwayNumber), eq(startDate), eq(endDate));

        assertNotNull(page);
        assertEquals(1, page.getTotalElements());

        InboundPutAwayResDto resDto = page.getContent().get(0);
        assertEquals(Long.valueOf(1L), resDto.getInboundId());
        assertEquals("STATUS", resDto.getInboundStatus());
        assertEquals("SCH001", resDto.getScheduleNumber());
        assertEquals("CHK001", resDto.getInboundCheckNumber());
        assertEquals("PA001", resDto.getPutAwayNumber());
        assertEquals(putAwayDate, LocalDate.parse(resDto.getPutAwayDate()));
        assertEquals(Long.valueOf(10L), resDto.getOrderId());
        assertEquals("ORD001", resDto.getOrderNumber());
        assertEquals(orderDate, LocalDate.parse(resDto.getOrderDate()));
        assertEquals(Long.valueOf(100L), resDto.getSupplierId());
        assertEquals("SupplierA", resDto.getSupplierName());

        assertNotNull(resDto.getLotList());
        assertEquals(1, resDto.getLotList().size());
        LotResDto resultLot = resDto.getLotList().get(0);
        assertEquals(Long.valueOf(200L), resultLot.getLotId());
        assertEquals("P001", resultLot.getProductCode());
    }

    @Test
    public void testGetFilteredPutAway_Empty() {
        when(inboundRetrievalPort.findFilteredInboundPutAway(eq(inboundPutAwayNumber), eq(startDate), eq(endDate), any(Pageable.class)))
                .thenReturn(Collections.emptyList());
        when(inboundRetrievalPort.countFilteredPutAway(eq(inboundPutAwayNumber), eq(startDate), eq(endDate)))
                .thenReturn(0);

        Page<InboundPutAwayResDto> page = service.getFilteredPutAway(inboundPutAwayNumber, startDate, endDate, pageable);

        verify(inboundRetrievalPort).findFilteredInboundPutAway(eq(inboundPutAwayNumber), eq(startDate), eq(endDate), any(Pageable.class));
        verify(inboundRetrievalPort).countFilteredPutAway(eq(inboundPutAwayNumber), eq(startDate), eq(endDate));

        assertNotNull(page);
        assertEquals(0, page.getTotalElements());
        assertTrue(page.getContent().isEmpty());
    }
}

package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.request.InboundReqDto;
import com.example.wms.inbound.application.domain.Inbound;
import com.example.wms.inbound.application.port.out.AssignInboundNumberPort;
import com.example.wms.inbound.application.port.out.CreateInboundPlanPort;
import com.example.wms.order.application.domain.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class CreateInboundPlanServiceTest {

    private CreateInboundPlanService createInboundPlanService;

    @Mock
    private CreateInboundPlanPort createInboundPlanPort;

    @Mock
    private AssignInboundNumberPort assignInboundNumberPort;

    private InboundReqDto inboundReqDto;
    private Order order;

    @Before
    public void setUp() {
        createInboundPlanService = new CreateInboundPlanService(createInboundPlanPort, assignInboundNumberPort);

        inboundReqDto = new InboundReqDto();
        inboundReqDto.setOrderId(1L);
        inboundReqDto.setSupplierId(1L);
        inboundReqDto.setScheduleDate(LocalDate.now());

        order = Order.builder()
                .orderId(1L)
                .supplierId(1L)
                .inboundDate(LocalDate.now())
                .build();

        doAnswer(invocation -> {
            Inbound inbound = invocation.getArgument(0);
            inbound.setInboundId(1L);
            return null;
        }).when(createInboundPlanPort).save(any(Inbound.class));
    }

    @Test
    public void createInboundPlan_Success() {

        // Given
        when(assignInboundNumberPort.findMaxISNumber()).thenReturn(null);

        // When
        Long result = createInboundPlanService.createInboundPlan(inboundReqDto);

        // Then
        assertEquals(Long.valueOf(1L), result);

        ArgumentCaptor<Inbound> inboundCaptor = ArgumentCaptor.forClass(Inbound.class);
        verify(createInboundPlanPort).save(inboundCaptor.capture());

        Inbound capturedInbound = inboundCaptor.getValue();
        assertEquals("입하예정", capturedInbound.getInboundStatus());
        assertEquals(inboundReqDto.getOrderId(), capturedInbound.getOrderId());
        assertEquals(inboundReqDto.getSupplierId(), capturedInbound.getSupplierId());
        assertEquals(inboundReqDto.getScheduleDate(), capturedInbound.getScheduleDate());
        assertNotNull(capturedInbound.getScheduleNumber());
    }

    @Test
    public void createInboundPlan_WithExistingNumber() {
        // Given
        String existingNumber = "IS202502230001";
        when(assignInboundNumberPort.findMaxISNumber()).thenReturn(existingNumber);

        // When
        createInboundPlanService.createInboundPlan(inboundReqDto);

        // Then
        ArgumentCaptor<Inbound> inboundCaptor = ArgumentCaptor.forClass(Inbound.class);
        verify(createInboundPlanPort).save(inboundCaptor.capture());

        Inbound capturedInbound = inboundCaptor.getValue();
        String expectedNumber = "IS" + LocalDate.now().toString().replace("-","") + "0002";
        assertEquals(expectedNumber, capturedInbound.getScheduleNumber());
    }

    @Test
    public void createInboundSchedule_Success() {
        // Given
        when(assignInboundNumberPort.findMaxISNumber()).thenReturn(null);

        // When
        createInboundPlanService.createInboundSchedule(order);

        // Then
        ArgumentCaptor<Inbound> inboundCaptor = ArgumentCaptor.forClass(Inbound.class);
        verify(createInboundPlanPort).save(inboundCaptor.capture());

        Inbound capturedInbound = inboundCaptor.getValue();
        assertEquals("입하예정", capturedInbound.getInboundStatus());
        assertEquals(order.getOrderId(), capturedInbound.getOrderId());
        assertEquals(order.getSupplierId(), capturedInbound.getSupplierId());
        assertEquals(order.getInboundDate(), capturedInbound.getScheduleDate());
        assertNotNull(capturedInbound.getScheduleNumber());
    }

    @Test
    public void verifyScheduleNumberFormat() {

        // Given
        when(assignInboundNumberPort.findMaxISNumber()).thenReturn(null);
        LocalDate today = LocalDate.now();
        String expectedDateString = today.toString().replace("-","");

        // When
        createInboundPlanService.createInboundPlan(inboundReqDto);

        // Then
        ArgumentCaptor<Inbound> inboundCaptor = ArgumentCaptor.forClass(Inbound.class);
        verify(createInboundPlanPort).save(inboundCaptor.capture());

        Inbound capturedInbound = inboundCaptor.getValue();
        String scheduleNumber = capturedInbound.getScheduleNumber();

        // Verify format: "IS" + YYYYMMDD + "0001"
        assertEquals("IS", scheduleNumber.substring(0, 2));
        assertEquals(expectedDateString, scheduleNumber.substring(2, 10));
        assertEquals("0000", scheduleNumber.substring(10));
    }

}
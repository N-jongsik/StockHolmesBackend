package com.example.wms.inbound.application.service;


import com.example.wms.inbound.application.domain.Inbound;
import com.example.wms.inbound.application.port.out.AssignInboundNumberPort;
import com.example.wms.inbound.application.port.out.InboundPort;
import com.example.wms.infrastructure.exception.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreateInboundPutAwayServiceTest {

    private CreateInboundPutAwayService createInboundPutAwayService;

    @Mock
    private InboundPort inboundPort;

    @Mock
    private AssignInboundNumberPort assignInboundNumberPort;

    private Long inboundId;
    private Inbound inbound;

    @Before
    public void setUp() {
        createInboundPutAwayService = new CreateInboundPutAwayService(inboundPort, assignInboundNumberPort);

        inboundId = 1L;
        inbound = Inbound.builder()
                .inboundId(inboundId)
                .inboundStatus("입하검사")
                .build();
    }

    @Test
    public void createPutAway_Success() {
        // Given
        when(inboundPort.findById(inboundId)).thenReturn(inbound);
        when(assignInboundNumberPort.findMaxPANumber()).thenReturn(null);

        // When
        createInboundPutAwayService.createPutAway(inboundId);

        // Then
        String expectedNumber = "PA" + LocalDate.of(2025,2,24).toString().replace("-","") + "0000";
        verify(inboundPort).updatePA(
                eq(inboundId),
                eq(LocalDate.of(2025,2,24)),
                eq(expectedNumber),
                eq("입고적치")
        );
    }

    @Test(expected = NotFoundException.class)
    public void createPutAway_InboundNotFound() {
        // Given
        when(inboundPort.findById(inboundId)).thenReturn(null);

        // When
        createInboundPutAwayService.createPutAway(inboundId);
    }

    @Test
    public void createPutAway_WithExistingNumber() {
        // Given
        when(inboundPort.findById(inboundId)).thenReturn(inbound);
        String existingNumber = "PA202502230001";
        when(assignInboundNumberPort.findMaxPANumber()).thenReturn(existingNumber);

        // When
        createInboundPutAwayService.createPutAway(inboundId);

        // Then
        String expectedNumber = "PA" + LocalDate.of(2025,2,24).toString().replace("-","") + "0002";
        verify(inboundPort).updatePA(
                eq(inboundId),
                any(LocalDate.class),
                eq(expectedNumber),
                eq("입고적치")
        );
    }

    @Test
    public void verifyPutAwayNumberFormat() {
        // Given
        when(inboundPort.findById(inboundId)).thenReturn(inbound);
        when(assignInboundNumberPort.findMaxPANumber()).thenReturn(null);
        LocalDate today = LocalDate.now();

        String expectedDateString = today.toString().replace("-","");

        // When
        createInboundPutAwayService.createPutAway(inboundId);

        // Then
        verify(inboundPort).updatePA(
                eq(inboundId),
                any(LocalDate.class),
                argThat(number ->
                        number.startsWith("PA") &&
                                number.substring(2, 10).equals(expectedDateString) &&
                                number.endsWith("0000")
                ),
                eq("입고적치")
        );
    }

    @Test
    public void createPutAway_VerifyAllFields() {
        // Given
        when(inboundPort.findById(inboundId)).thenReturn(inbound);
        when(assignInboundNumberPort.findMaxPANumber()).thenReturn(null);

        // When
        createInboundPutAwayService.createPutAway(inboundId);

        // Then
        verify(inboundPort).updatePA(
                eq(inboundId),
                eq(LocalDate.now()),
                argThat(number -> number.startsWith("PA")),
                eq("입고적치")
        );
    }


}
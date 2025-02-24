package com.example.wms.inbound.application.service;

import com.example.wms.inbound.application.domain.Inbound;
import com.example.wms.inbound.application.port.out.InboundPort;
import com.example.wms.infrastructure.exception.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class DeleteInboundCheckServiceTest {

    private DeleteInboundCheckService deleteInboundCheckService;

    @Mock
    private InboundPort inboundPort;

    private Long inboundId;
    private Inbound inbound;

    @Before
    public void setUp() {
        deleteInboundCheckService = new DeleteInboundCheckService(inboundPort);

        inboundId = 1L;
        inbound = Inbound.builder()
                .inboundId(inboundId)
                .inboundStatus("입하검사")
                .build();
    }

    @Test
    public void deleteInboundCheck_Success() {

        when(inboundPort.findById(inboundId)).thenReturn(inbound);

        deleteInboundCheckService.deleteInboundCheck(inboundId);

        verify(inboundPort).updateIC(eq(inboundId), eq(null), eq(null), eq("입하예정"));
    }


    @Test(expected = NotFoundException.class)
    public void deleteInboundCheck_InboundNotFound() {
        when(inboundPort.findById(inboundId)).thenReturn(null);

        deleteInboundCheckService.deleteInboundCheck(inboundId);
    }
}
package com.example.wms.inbound.application.service;

import com.example.wms.inbound.application.domain.Inbound;
import com.example.wms.inbound.application.port.in.DeleteInboundCheckUseCase;
import com.example.wms.inbound.application.port.out.InboundPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InboundCheckDeleteTest {

    @InjectMocks
    private DeleteInboundCheckUseCase deleteInboundCheckService;

    @Mock
    private InboundPort inboundPort;

    @Test
    @DisplayName("입하 검사 데이터를 정상적으로 삭제할 수 있다.")
    public void testDeleteInboundCheck_Success() {

        // given
        Long inboundId = 1L;

        Inbound inbound = Inbound.builder()
                .inboundId(inboundId)
                .inboundStatus("입하검사")
                .checkNumber("IC202502050001")
                .checkDate(LocalDate.of(2025,2,10))
                .build();

        when(inboundPort.findById(inboundId)).thenReturn(inbound);

        deleteInboundCheckService.deleteInboundCheck(inboundId);
        inbound.setInboundStatus("입하예정");
        assertEquals("입하예정",inbound.getInboundStatus());

        verify(inboundPort, times(1)).updateIC(eq(inboundId), eq(null), eq(null), eq("입하예정"));

    }
}

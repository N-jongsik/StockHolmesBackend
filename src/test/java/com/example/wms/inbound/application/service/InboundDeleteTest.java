package com.example.wms.inbound.application.service;

import com.example.wms.inbound.application.port.out.InboundPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class InboundDeleteTest {

    @InjectMocks
    private InboundService inboundService;

    @Mock
    private InboundPort inboundPort;

    @Test
    @DisplayName("입하 예정을 삭제하면 InboundPort의 delete 메서드가 호출되어야 한다.")
    public void testDeleteInboundPlan() {
        // given
        Long inboundId = 1L;

        inboundService.deleteInboundPlan(inboundId);

        verify(inboundPort, times(1)).delete(inboundId);
    }

}

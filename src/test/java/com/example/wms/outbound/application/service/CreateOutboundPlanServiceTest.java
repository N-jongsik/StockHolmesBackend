package com.example.wms.outbound.application.service;

import com.example.wms.outbound.adapter.in.dto.OutboundPlanRequestDto;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.out.CalculateOsNumberPort;
import com.example.wms.outbound.application.port.out.CreateOutboundPlanPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateOutboundPlanServiceTest {

    @Mock
    private CreateOutboundPlanPort createOutboundPlanPort;

    @Mock
    private CalculateOsNumberPort calculateOsNumberPort;

    @InjectMocks
    private CreateOutboundPlanService createOutboundPlanService;

    private OutboundPlanRequestDto outboundPlanRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        outboundPlanRequestDto = OutboundPlanRequestDto.builder()
                .planDate(LocalDate.of(2025, 2, 10))
                .outboundScheduleDate(LocalDate.of(2025, 2, 15))
                .productionPlanNumber("PP12345")
                .build();
    }

    @Test
    @DisplayName("출고 예정 번호가 없을 때 기본값 0000으로 시작하는지 테스트")
    void testCreateOutbound_WhenNoExistingOsNumber() {
        // given
        String currentDate = LocalDate.now().toString().replace("-", "");
        when(calculateOsNumberPort.findMaxOutboundScheduleNumber()).thenReturn(null);

        // when
        Long outboundPlanId = createOutboundPlanService.createOutbound(outboundPlanRequestDto);

        // then
        assertNull(outboundPlanId);
        verify(createOutboundPlanPort, times(1)).save(any(OutboundPlan.class));

        // 저장된 데이터 검증
        ArgumentCaptor<OutboundPlan> captor = ArgumentCaptor.forClass(OutboundPlan.class);
        verify(createOutboundPlanPort).save(captor.capture());

        OutboundPlan savedPlan = captor.getValue();
        assertEquals("OS"+currentDate+"0000", savedPlan.getOutboundScheduleNumber()); // 기본값 0000
    }

    @Test
    @DisplayName("정상적인 출고 예정 생성 테스트")
    void testCreateOutbound() {
        // given
        String currentDate = LocalDate.now().toString().replace("-", "");
        when(calculateOsNumberPort.findMaxOutboundScheduleNumber()).thenReturn("OS"+currentDate+"0000");

        // when
        Long outboundPlanId = createOutboundPlanService.createOutbound(outboundPlanRequestDto);

        // then
        verify(createOutboundPlanPort, times(1)).save(any(OutboundPlan.class));

        // 저장된 데이터 검증
        ArgumentCaptor<OutboundPlan> captor = ArgumentCaptor.forClass(OutboundPlan.class);
        verify(createOutboundPlanPort).save(captor.capture());

        OutboundPlan savedPlan = captor.getValue();
        assertEquals("진행중", savedPlan.getStatus());
        assertEquals("OS"+currentDate+"0001",savedPlan.getOutboundScheduleNumber());
        assertEquals(outboundPlanRequestDto.getPlanDate(), savedPlan.getPlanDate());
        assertEquals(outboundPlanRequestDto.getOutboundScheduleDate(), savedPlan.getOutboundScheduleDate());
        assertEquals(outboundPlanRequestDto.getProductionPlanNumber(), savedPlan.getProductionPlanNumber());
    }
}

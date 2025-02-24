package com.example.wms.inbound.application.service;

import com.example.wms.inbound.application.port.out.DeleteInboundPlanPort;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DeleteInboundPlanServiceTest {

    private DeleteInboundPlanService deleteInboundPlanService;

    @Mock
    private DeleteInboundPlanPort deleteInboundPlanPort;

    @Before
    public void setUp() {
        deleteInboundPlanService = new DeleteInboundPlanService(deleteInboundPlanPort);
    }

    @Test
    public void deleteInboundPlan_ShouldCallPortDelete() {
        // Given
        Long inboundId = 1L;

        // When
        deleteInboundPlanService.deleteInboundPlan(inboundId);

        // Then
        verify(deleteInboundPlanPort).delete(inboundId);
    }
}

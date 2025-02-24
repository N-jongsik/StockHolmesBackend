package com.example.wms.inbound.application.service;

import com.example.wms.product.application.port.in.ProductUseCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InboundPerformAssignServiceTest {

    @Mock
    private ProductUseCase productUseCase;

    private InboundPerformAssignService service;

    @Before
    public void setUp() {
        service = new InboundPerformAssignService(productUseCase);
    }

    @Test
    public void testSchedule() {
        // When
        service.schedule();

        verify(productUseCase, times(1)).performABCAnalysis();
        verify(productUseCase, times(1)).assignLocationBinCode();
    }
}

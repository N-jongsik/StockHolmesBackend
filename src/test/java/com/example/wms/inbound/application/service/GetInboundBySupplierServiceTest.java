package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.response.SupplierInboundResDto;
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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetInboundBySupplierServiceTest {

    private GetInboundBySupplierService getInboundBySupplierService;

    @Mock
    private InboundRetrievalPort inboundRetrievalPort;

    private LocalDate startDate;
    private LocalDate endDate;
    private Pageable pageable;

    @Before
    public void setUp() {
        getInboundBySupplierService = new GetInboundBySupplierService(inboundRetrievalPort);
        startDate = LocalDate.of(2025, 2, 1);
        endDate = LocalDate.of(2025, 2, 28);
        pageable = PageRequest.of(0, 10);
    }

    @Test
    public void getAllInboundBySupplierWithPagination_ShouldReturnPage() {
        SupplierInboundResDto dto1 = new SupplierInboundResDto();
        SupplierInboundResDto dto2 = new SupplierInboundResDto();
        List<SupplierInboundResDto> inboundList = Arrays.asList(dto1, dto2);

        when(inboundRetrievalPort.findAllInboundBySupplierWithPagination(startDate, endDate, pageable))
                .thenReturn(inboundList);

        Page<SupplierInboundResDto> result = getInboundBySupplierService
                .getAllInboundBySupplierWithPagination(startDate, endDate, pageable);

        verify(inboundRetrievalPort).findAllInboundBySupplierWithPagination(startDate, endDate, pageable);
        assertNotNull(result);
        assertEquals(inboundList.size(), result.getTotalElements());
        assertEquals(inboundList, result.getContent());
        assertEquals(pageable, result.getPageable());
    }
}

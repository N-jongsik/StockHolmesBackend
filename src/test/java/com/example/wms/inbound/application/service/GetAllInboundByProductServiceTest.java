package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.response.ProductInboundResDto;
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
public class GetAllInboundByProductServiceTest {

    private GetAllInboundByProductService getAllInboundByProductService;

    @Mock
    private InboundRetrievalPort inboundRetrievalPort;

    private LocalDate startDate;
    private LocalDate endDate;
    private Pageable pageable;

    @Before
    public void setUp() {
        getAllInboundByProductService = new GetAllInboundByProductService(inboundRetrievalPort);
        startDate = LocalDate.of(2025, 2, 1);
        endDate = LocalDate.of(2025, 2, 28);
        pageable = PageRequest.of(0, 10);
    }

    @Test
    public void getAllInboundByProductWithPagination_ShouldReturnPage() {
        ProductInboundResDto dto1 = new ProductInboundResDto();
        ProductInboundResDto dto2 = new ProductInboundResDto();
        List<ProductInboundResDto> inboundList = Arrays.asList(dto1, dto2);
        when(inboundRetrievalPort.findAllInboundByProductWithPagination(startDate, endDate, pageable))
                .thenReturn(inboundList);

        Page<ProductInboundResDto> result = getAllInboundByProductService
                .getAllInboundByProductWithPagination(startDate, endDate, pageable);

        verify(inboundRetrievalPort).findAllInboundByProductWithPagination(startDate, endDate, pageable);
        assertNotNull(result);
        assertEquals(inboundList.size(), result.getTotalElements());
        assertEquals(inboundList, result.getContent());
        assertEquals(pageable, result.getPageable());
    }
}

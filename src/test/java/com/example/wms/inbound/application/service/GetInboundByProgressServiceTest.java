package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.response.InboundProgressDetailDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundProgressResDto;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GetInboundByProgressServiceTest {

    private GetInboundByProgressService getInboundByProgressService;

    @Mock
    private InboundRetrievalPort inboundRetrievalPort;

    private LocalDate startDate;
    private LocalDate endDate;
    private Pageable pageable;

    @Before
    public void setUp() {
        getInboundByProgressService = new GetInboundByProgressService(inboundRetrievalPort);
        startDate = LocalDate.of(2025, 2, 1);
        endDate = LocalDate.of(2025, 2, 28);
        pageable = PageRequest.of(0, 10);
    }

    @Test
    public void getAllInboundProgressWithPagination_ShouldReturnProperlyFilteredPage() {
        InboundProgressDetailDto scheduleDto = mock(InboundProgressDetailDto.class);
        when(scheduleDto.getCheckNumber()).thenReturn(null);
        when(scheduleDto.getPutAwayNumber()).thenReturn(null);

        InboundProgressDetailDto checkDto = mock(InboundProgressDetailDto.class);
        when(checkDto.getCheckNumber()).thenReturn("CHK123");
        when(checkDto.getPutAwayNumber()).thenReturn(null);

        InboundProgressDetailDto putAwayDto = mock(InboundProgressDetailDto.class);
        when(putAwayDto.getCheckNumber()).thenReturn("CHK456");
        when(putAwayDto.getPutAwayNumber()).thenReturn("PA789");

        List<InboundProgressDetailDto> inboundList = Arrays.asList(scheduleDto, checkDto, putAwayDto);
        when(inboundRetrievalPort.findAllInboundProgressWithPagination(startDate, endDate, pageable))
                .thenReturn(inboundList);

        Page<InboundProgressResDto> resultPage = getInboundByProgressService
                .getAllInboundProgressWithPagination(startDate, endDate, pageable);

        verify(inboundRetrievalPort).findAllInboundProgressWithPagination(startDate, endDate, pageable);

        assertNotNull(resultPage);
        assertEquals(1, resultPage.getTotalElements());

        InboundProgressResDto progressResDto = resultPage.getContent().get(0);
        assertNotNull(progressResDto);

        List<InboundProgressDetailDto> scheduleList = progressResDto.getScheduleList();
        List<InboundProgressDetailDto> checkList = progressResDto.getCheckList();
        List<InboundProgressDetailDto> putAwayList = progressResDto.getPutAwayList();

        assertNotNull(scheduleList);
        assertNotNull(checkList);
        assertNotNull(putAwayList);

        assertEquals(1, scheduleList.size());
        assertEquals(1, checkList.size());
        assertEquals(1, putAwayList.size());

        assertSame(scheduleDto, scheduleList.get(0));
        assertSame(checkDto, checkList.get(0));
        assertSame(putAwayDto, putAwayList.get(0));
    }
}

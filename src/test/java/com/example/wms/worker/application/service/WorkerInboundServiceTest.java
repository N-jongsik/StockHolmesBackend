package com.example.wms.worker.application.service;

import com.example.wms.worker.adapter.in.dto.response.WorkerInboundResDto;
import com.example.wms.worker.adapter.in.dto.response.WorkerProductResDto;
import com.example.wms.worker.application.port.out.WorkerInboundPort;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkerInboundServiceTest {

    @Mock
    WorkerInboundPort workerInboundPort;

    @InjectMocks
    WorkerInboundService workerInboundService;

    private List<WorkerInboundResDto> mockWorkerInboundList;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        List <WorkerProductResDto> productList = Arrays.asList(
                new WorkerProductResDto(1L, "p-0123","tire"),
                new WorkerProductResDto(2L,"p-0124","tire2")
        );

        mockWorkerInboundList = Arrays.asList(
                new WorkerInboundResDto(1L, productList)
        );
    }

    @Test
    @DisplayName("작업자 입하 예정 리스트 조회를 테스트합니다.")
   public void testWorkerGetInboundList() {

        // given
        LocalDate todayDate = LocalDate.now();

        when(workerInboundPort.findFilteredWorkerInboundList(eq(todayDate)))
                .thenReturn(mockWorkerInboundList);
        // when
        List<WorkerInboundResDto> result = workerInboundService.getFilteredWorkerInboundList(LocalDate.now());

        // then
        assertThat(result).isNotNull();


        verify(workerInboundPort, times(1)).findFilteredWorkerInboundList(todayDate);
    }

}

package com.example.wms.worker.application.service;

import com.example.wms.worker.adapter.in.dto.response.WorkerInboundResDto;
import com.example.wms.worker.adapter.in.dto.response.WorkerProductResDto;
import com.example.wms.worker.application.port.out.WorkerInboundPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkerInboundServiceTest {

    @Mock
    WorkerInboundPort workerInboundPort;

    @InjectMocks
    WorkerInboundService workerInboundService;

    private List<WorkerInboundResDto> mockWorkerInboundList;

    @BeforeEach
    void setUp() {
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
    void testWorkerGetInboundList() {

        // given
        LocalDate startDate = LocalDate.of(2025,2,21);
        LocalDate endDate = LocalDate.of(2025,2,22);
        Pageable pageable = PageRequest.of(0,10);

        when(workerInboundPort.findFilteredWorkerInboundList(eq(startDate), eq(endDate), any(Pageable.class)))
                .thenReturn(mockWorkerInboundList);
        when(workerInboundPort.countFilteredWorkerInboundList(eq(startDate),eq(endDate)))
                .thenReturn(mockWorkerInboundList.size());

        // when
        Page<WorkerInboundResDto> result = workerInboundService.getFilteredWorkerInboundList(startDate, endDate, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(mockWorkerInboundList.size());
        assertThat(result.getTotalElements()).isEqualTo(mockWorkerInboundList.size());

        verify(workerInboundPort, times(1)).findFilteredWorkerInboundList(eq(startDate), eq(endDate), any(Pageable.class));
        verify(workerInboundPort, times(1)).countFilteredWorkerInboundList(eq(startDate), eq(endDate));
    }

}

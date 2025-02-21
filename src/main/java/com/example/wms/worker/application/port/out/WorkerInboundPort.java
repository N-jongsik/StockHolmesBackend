package com.example.wms.worker.application.port.out;

import com.example.wms.worker.adapter.in.dto.response.WorkerInboundResDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface WorkerInboundPort {
    Integer countFilteredWorkerInboundList(LocalDate startDate, LocalDate endDate);
    List<WorkerInboundResDto> findFilteredWorkerInboundList(LocalDate startDate, LocalDate endDate, Pageable pageable);
}

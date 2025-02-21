package com.example.wms.worker.application.port.in;

import com.example.wms.worker.adapter.in.dto.response.WorkerInboundResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface WorkerInboundUseCase {
    Page<WorkerInboundResDto> getFilteredWorkerInboundList(LocalDate startDate, LocalDate endDate, Pageable pageable);
}

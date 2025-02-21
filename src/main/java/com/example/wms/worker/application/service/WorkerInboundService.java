package com.example.wms.worker.application.service;

import com.example.wms.infrastructure.pagination.util.PageableUtils;
import com.example.wms.worker.adapter.in.dto.response.WorkerInboundResDto;
import com.example.wms.worker.application.port.in.WorkerInboundUseCase;
import com.example.wms.worker.application.port.out.WorkerInboundPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkerInboundService implements WorkerInboundUseCase {

    private final WorkerInboundPort workerInboundPort;

    @Override
//    @Transactional(readOnly = true)
    public List<WorkerInboundResDto> getFilteredWorkerInboundList(LocalDate todayDate) {
        List<WorkerInboundResDto> workerInboundList = workerInboundPort.findFilteredWorkerInboundList(todayDate);
        return workerInboundList;
    }
}

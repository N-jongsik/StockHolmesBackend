package com.example.wms.worker.adapter.out;

import com.example.wms.infrastructure.mapper.WorkerInboundMapper;
import com.example.wms.worker.application.port.out.WorkerInboundPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class WorkerInboundAdapter implements WorkerInboundPort {

    private WorkerInboundMapper workerInboundMapper;

    @Override
    public Integer countFilteredWorkerInboundList(LocalDate startDate, LocalDate endDate) {
        return workerInboundMapper.countFilteredWorkerInboundList(startDate, endDate);
    }
}

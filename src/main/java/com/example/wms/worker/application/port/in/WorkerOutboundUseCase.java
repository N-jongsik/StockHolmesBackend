package com.example.wms.worker.application.port.in;

import com.example.wms.worker.adapter.in.dto.WorkerOutboundDto;

import java.util.List;

public interface WorkerOutboundUseCase {
    List<WorkerOutboundDto> getWorkerOutboundPlans();
}

package com.example.wms.worker.application.port.out;

import java.time.LocalDate;

public interface WorkerInboundPort {
    Integer countFilteredWorkerInboundList(LocalDate startDate, LocalDate endDate);
}

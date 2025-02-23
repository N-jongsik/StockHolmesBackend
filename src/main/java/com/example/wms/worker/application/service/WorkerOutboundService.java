package com.example.wms.worker.application.service;

import com.example.wms.worker.application.port.out.WorkerOutboundPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkerOutboundService {
    private final WorkerOutboundPort workerOutboundPort;


}

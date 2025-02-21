package com.example.wms.worker.application.port.in;

import com.example.wms.worker.adapter.in.dto.request.WorkerInboundCheckReqDto;
import com.example.wms.worker.adapter.in.dto.response.WorkerInboundCheckResDto;
import com.example.wms.worker.adapter.in.dto.response.WorkerInboundResDto;

import java.time.LocalDate;
import java.util.List;

public interface WorkerInboundUseCase {
    List<WorkerInboundResDto> getFilteredWorkerInboundList(LocalDate todayDate);
    WorkerInboundCheckResDto createWorkerInboundCheck(Long inboundId, WorkerInboundCheckReqDto dto);
}

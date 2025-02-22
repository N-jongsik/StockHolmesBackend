package com.example.wms.worker.adapter.out;

import com.example.wms.infrastructure.mapper.WorkerInboundMapper;
import com.example.wms.worker.adapter.in.dto.request.WorkerInboundCheckProductReqDto;
import com.example.wms.worker.adapter.in.dto.response.WorkerInboundCheckResDto;
import com.example.wms.worker.adapter.in.dto.response.WorkerInboundResDto;
import com.example.wms.worker.application.port.out.WorkerInboundPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WorkerInboundAdapter implements WorkerInboundPort {

    private final WorkerInboundMapper workerInboundMapper;

    @Override
    public List<WorkerInboundResDto> findFilteredWorkerInboundList(LocalDate todayDate) {
        return workerInboundMapper.findFilteredWorkerInboundList(todayDate);
    }

    @Override
    public WorkerInboundCheckResDto processInboundCheck(Long inboundId, List<WorkerInboundCheckProductReqDto> dto) {
        return workerInboundMapper.processInboundCheck(inboundId, dto);
    }

    @Override
    public void updateLotDefectiveStatus(Long productId, Boolean isDefective) {
        workerInboundMapper.updateLotDefectiveStatus( productId, isDefective);
    }


}

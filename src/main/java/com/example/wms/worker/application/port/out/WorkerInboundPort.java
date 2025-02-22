package com.example.wms.worker.application.port.out;

import com.example.wms.worker.adapter.in.dto.request.WorkerInboundCheckProductReqDto;
import com.example.wms.worker.adapter.in.dto.response.WorkerInboundCheckResDto;
import com.example.wms.worker.adapter.in.dto.response.WorkerInboundResDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface WorkerInboundPort {
    List<WorkerInboundResDto> findFilteredWorkerInboundList(LocalDate todayDate);
    WorkerInboundCheckResDto processInboundCheck(Long inboundId, List<WorkerInboundCheckProductReqDto> dto);
    void updateLotDefectiveStatus(Long productId, Boolean isDefective);

}


package com.example.wms.inbound.application.port.in;

import com.example.wms.inbound.adapter.in.dto.response.InboundProgressResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface GetInboundByProgressUseCase {
    Page<InboundProgressResDto> getAllInboundProgressWithPagination(LocalDate startDate, LocalDate endDate, Pageable pageable);

}

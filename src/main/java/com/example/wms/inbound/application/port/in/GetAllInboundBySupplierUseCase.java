package com.example.wms.inbound.application.port.in;

import com.example.wms.inbound.adapter.in.dto.response.SupplierInboundResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface GetAllInboundBySupplierUseCase {
    Page<SupplierInboundResDto> getAllInboundBySupplierWithPagination(LocalDate startDate, LocalDate endDate, Pageable pageable);

}

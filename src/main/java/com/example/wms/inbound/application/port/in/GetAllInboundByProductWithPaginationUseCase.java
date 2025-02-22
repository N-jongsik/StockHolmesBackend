package com.example.wms.inbound.application.port.in;

import com.example.wms.inbound.adapter.in.dto.response.ProductInboundResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface GetAllInboundByProductWithPaginationUseCase {
    Page<ProductInboundResDto> getAllInboundByProductWithPagination(LocalDate startDate, LocalDate endDate, Pageable pageable);

}

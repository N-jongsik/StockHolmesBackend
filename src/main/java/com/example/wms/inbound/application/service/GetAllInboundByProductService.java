package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.response.ProductInboundResDto;
import com.example.wms.inbound.application.port.in.GetAllInboundByProductWithPaginationUseCase;
import com.example.wms.inbound.application.port.out.InboundRetrievalPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllInboundByProductService implements GetAllInboundByProductWithPaginationUseCase {

    private final InboundRetrievalPort inboundRetrievalPort;

    @Override
    public Page<ProductInboundResDto> getAllInboundByProductWithPagination(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        List<ProductInboundResDto> inboundList = inboundRetrievalPort.findAllInboundByProductWithPagination(startDate, endDate, pageable);
        return new PageImpl<>(inboundList, pageable, inboundList.size());
    }
}


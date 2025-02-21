package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.response.SupplierInboundResDto;
import com.example.wms.inbound.application.port.in.GetAllInboundBySupplierUseCase;
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
public class GetInboundBySupplierService implements GetAllInboundBySupplierUseCase {

    private final InboundRetrievalPort inboundRetrievalPort;

    @Override
    public Page<SupplierInboundResDto> getAllInboundBySupplierWithPagination(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        List<SupplierInboundResDto> inboundList = inboundRetrievalPort.findAllInboundBySupplierWithPagination(startDate, endDate, pageable);
        return new PageImpl<>(inboundList, pageable, inboundList.size());
    }
}

package com.example.wms.order.application.port.in;

import com.example.wms.order.adapter.in.dto.SupplierOverviewDto;
import com.example.wms.order.adapter.in.dto.SupplierResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SupplierUseCase {

    Page<SupplierResponseDto> getAllSuppliers(String businessNumber, Pageable pageable);
    List<SupplierOverviewDto> getAllSupplierOverviews();
}

package com.example.wms.order.application.port.out;

import com.example.wms.order.adapter.in.dto.ProductInSupplierDto;
import com.example.wms.order.adapter.in.dto.SupplierOverviewDto;
import com.example.wms.order.adapter.in.dto.SupplierResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SupplierPort {
    long countAllSuppliers(String businessNumber);
    List<SupplierResponseDto> findSupplierWithPagination(String businessNumber, Pageable pageable);
    List<ProductInSupplierDto> findProductsBySupplierIds(List<Long> supplierIds);
    List<SupplierOverviewDto> findProductOverview();
}

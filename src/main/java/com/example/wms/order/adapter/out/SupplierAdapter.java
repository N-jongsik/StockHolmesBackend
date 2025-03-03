package com.example.wms.order.adapter.out;

import com.example.wms.infrastructure.mapper.SupplierMapper;
import com.example.wms.order.adapter.in.dto.ProductInSupplierDto;
import com.example.wms.order.adapter.in.dto.SupplierOverviewDto;
import com.example.wms.order.adapter.in.dto.SupplierResponseDto;
import com.example.wms.order.application.port.out.SupplierPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SupplierAdapter implements SupplierPort {

    private final SupplierMapper supplierMapper;

    @Override
    public long countAllSuppliers(String businessNumber) {
        return supplierMapper.countAllSuppliers(businessNumber);
    }

    public List<SupplierResponseDto> findSupplierWithPagination(String businessNumber, Pageable pageable) {
        return supplierMapper.findSupplierWithPagination(businessNumber, pageable);
    }
    public List<ProductInSupplierDto> findProductsBySupplierIds(List<Long> supplierIds) {
        return supplierMapper.findProductsBySupplierIds(supplierIds);
    }

    @Override
    public List<SupplierOverviewDto> findProductOverview() {
        return supplierMapper.findSupplierOverview();
    }
}

package com.example.wms.order.application.service;

import com.example.wms.infrastructure.pagination.util.PageableUtils;
import com.example.wms.order.adapter.in.dto.ProductInSupplierDto;
import com.example.wms.order.adapter.in.dto.SupplierOverviewDto;
import com.example.wms.order.adapter.in.dto.SupplierResponseDto;
import com.example.wms.order.application.domain.Supplier;
import com.example.wms.order.application.port.in.SupplierUseCase;
import com.example.wms.order.application.port.out.SupplierPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierService implements SupplierUseCase {

    private final SupplierPort supplierPort;

    @Override
    @Transactional(readOnly = true)
    public Page<SupplierResponseDto> getAllSuppliers(String businessNumber, Pageable pageable) {
        Pageable safePageable = PageableUtils.convertToSafePageableStrict(pageable, Supplier.class);

        List<SupplierResponseDto> supplierList = supplierPort.findSupplierWithPagination(businessNumber, safePageable);
        long count = supplierPort.countAllSuppliers(businessNumber);

        // supplierId 목록 추출
        List<Long> supplierIdList = supplierList.stream().map(SupplierResponseDto::getSupplierId).toList();

        // supplierId 목록에 해당하는 모든 product 조회
        List<ProductInSupplierDto> productsBySupplierIds = supplierPort.findProductsBySupplierIds(supplierIdList);

        // supplierId 기준으로 product 그룹핑
        Map<Long, List<ProductInSupplierDto>> productMap = productsBySupplierIds.stream()
                .collect(Collectors.groupingBy(ProductInSupplierDto::getSupplierId));

        // 각 SupplierResponseDto에 해당하는 productList 할당
        for (SupplierResponseDto supplier : supplierList) {
            supplier.setProductList(productMap.getOrDefault(supplier.getSupplierId(), new ArrayList<>()));
        }

        return new PageImpl<>(supplierList, safePageable, count);
    }

    @Override
    public List<SupplierOverviewDto> getAllSupplierOverviews() {
        return supplierPort.findProductOverview();
    }
}

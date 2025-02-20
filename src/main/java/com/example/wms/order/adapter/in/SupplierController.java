package com.example.wms.order.adapter.in;

import com.example.wms.order.adapter.in.dto.SupplierOverviewDto;
import com.example.wms.order.adapter.in.dto.SupplierResponseDto;
import com.example.wms.order.application.port.in.SupplierUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/supplier")
@Tag(name = "Supplier", description = "Supplier 관련 API")
public class SupplierController {

    private final SupplierUseCase supplierUseCase;

    @GetMapping
    @Operation(
            summary = "전체 공급업체 조회",
            description = "전체 공급업체 목록을 페이지별로 조회할 수 있습니다.\n" +
            "- **사업자 번호 필터링:** 요청 파라미터로 `businessNumber` 값을 전달하면 해당 문자열이 포함된 납품처만 조회합니다.\n" +
            "- **전체 조회:** `businessNumber`가 null 또는 빈 문자열일 경우 전체 납품처를 조회합니다.\n"
    )
    ResponseEntity<Page<SupplierResponseDto>> getAllSuppliers(@RequestParam(required = false) String businessNumber, @ParameterObject Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(supplierUseCase.getAllSuppliers(businessNumber, pageable));
    }

    @GetMapping("/list")
    @Operation(
            summary = "공급업체 간단 조회",
            description = "전체 공급업체 목록의 일부 컬럼(supplierId, supplierName)만 조회합니다."
    )
    ResponseEntity<List<SupplierOverviewDto>> getAllSupplierOverviews() {
        return ResponseEntity.status(HttpStatus.OK).body(supplierUseCase.getAllSupplierOverviews());
    }
}

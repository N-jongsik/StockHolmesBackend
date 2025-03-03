package com.example.wms.order.application.service;

import com.example.wms.order.adapter.in.dto.ProductInSupplierDto;
import com.example.wms.order.adapter.in.dto.SupplierResponseDto;
import com.example.wms.order.application.port.out.SupplierPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SupplierServiceTest {

    @Mock
    SupplierPort supplierPort;

    @InjectMocks
    SupplierService supplierService;

    private Pageable pageable;

    @BeforeEach
    void setUp() {
        // PageRequest.of(0, 10) 등으로 테스트용 Pageable 생성
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void getAllSuppliers_shouldReturnSuppliersWithProducts() {

        // given: SupplierResponseDto 샘플 데이터 생성
        SupplierResponseDto supplier1 = new SupplierResponseDto();
        supplier1.setSupplierId(1L);
        SupplierResponseDto supplier2 = new SupplierResponseDto();
        supplier2.setSupplierId(2L);
        List<SupplierResponseDto> supplierList = Arrays.asList(supplier1, supplier2);

        // 모킹: findSupplierWithPagination()와 countAllSuppliers()
        when(supplierPort.findSupplierWithPagination(eq(null), any(Pageable.class))).thenReturn(supplierList);
        when(supplierPort.countAllSuppliers(null)).thenReturn(2L);

        // given: ProductInSupplierDto 샘플 데이터 생성 (각 supplier에 해당하는 상품 목록)
        ProductInSupplierDto product1 = new ProductInSupplierDto();
        product1.setSupplierId(1L);
        ProductInSupplierDto product2 = new ProductInSupplierDto();
        product2.setSupplierId(1L);
        ProductInSupplierDto product3 = new ProductInSupplierDto();
        product3.setSupplierId(2L);
        List<ProductInSupplierDto> productList = Arrays.asList(product1, product2, product3);
        when(supplierPort.findProductsBySupplierIds(anyList())).thenReturn(productList);

        // when: 테스트 대상 메서드 호출
        Page<SupplierResponseDto> result = supplierService.getAllSuppliers(null, pageable);

        // then: 반환 결과 검증
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);


        // supplierId별 productList 확인
        SupplierResponseDto resultSupplier1 = result.getContent()
                .stream().filter(s -> s.getSupplierId().equals(1L)).findFirst().orElse(null);
        SupplierResponseDto resultSupplier2 = result.getContent()
                .stream().filter(s -> s.getSupplierId().equals(2L)).findFirst().orElse(null);

        assertThat(resultSupplier1)
                .isNotNull()
                .satisfies(s -> assertThat(s.getProductList()).hasSize(2));
        assertThat(resultSupplier2)
                .isNotNull()
                .satisfies(s -> assertThat(s.getProductList()).hasSize(1));
    }

    @Test
    void getAllSuppliers_whenNoProducts_thenReturnEmptyProductLists() {
        // given: SupplierResponseDto 샘플 데이터 생성
        SupplierResponseDto supplier1 = new SupplierResponseDto();
        supplier1.setSupplierId(1L);
        List<SupplierResponseDto> supplierList = Collections.singletonList(supplier1);

        // 모킹: supplierPort
        when(supplierPort.findSupplierWithPagination(eq(null), any(Pageable.class))).thenReturn(supplierList);
        when(supplierPort.countAllSuppliers(null)).thenReturn(1L);
        // 공급자에 매핑된 상품이 없는 경우 빈 리스트 반환
        when(supplierPort.findProductsBySupplierIds(anyList())).thenReturn(Collections.emptyList());

        // when
        Page<SupplierResponseDto> result = supplierService.getAllSuppliers(null, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);

        SupplierResponseDto resultSupplier = result.getContent().get(0);
        assertThat(resultSupplier)
                .isNotNull()
                .satisfies(s -> assertThat(s.getProductList()).isEmpty());
    }
}
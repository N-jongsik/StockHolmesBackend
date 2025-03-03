package com.example.wms.product.application.service;

import com.example.wms.product.adapter.in.dto.ProductResponseDto;
import com.example.wms.product.application.domain.Product;
import com.example.wms.product.application.port.out.ProductPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductPageableServiceTest {

    @Mock
    private ProductPort productPort;

    @InjectMocks
    private ProductService productService;

    @Test
    void getAllProducts() {
        //given
        String productCode = null;
        Pageable pageable = PageRequest.of(
                2,
                3,
                Sort.by("salePrice").ascending().and(Sort.by("purchasePrice").descending())
        );

        Product product1 = Product.builder()
                .productId(14L)
                .productCode("P1014")
                .productName("외장 하드")
                .purchasePrice(100000)
                .salePrice(140000)
                .lotUnit(15)
                .supplierId(114L)
                .stockLotCount(50)
                .category("컴퓨터 주변기기")
                .threshold(5)
                .leadTime(4)
                .locationBinCode("I10")
                .abcGrade("B")
                .build();

        Product product2 = Product.builder()
                .productId(11L)
                .productCode("P1011")
                .productName("블루투스 스피커")
                .purchasePrice(120000)
                .salePrice(180000)
                .lotUnit(8)
                .supplierId(111L)
                .stockLotCount(20)
                .category("액세서리")
                .threshold(4)
                .leadTime(5)
                .locationBinCode("F07")
                .abcGrade("C")
                .build();

        Product product3 = Product.builder()
                .productId(8L)
                .productCode("P1008")
                .productName("헤드폰")
                .purchasePrice(150000)
                .salePrice(220000)
                .lotUnit(10)
                .supplierId(108L)
                .stockLotCount(35)
                .category("액세서리")
                .threshold(5)
                .leadTime(6)
                .locationBinCode("C04")
                .abcGrade("B")
                .build();

        List<Product> expectedProducts = List.of(product1, product2, product3);
        long totalCount = 24L;

//        when(productPort.findProductWithPagination(eq(productCode), any(Pageable.class))).thenReturn(expectedProducts);
        when(productPort.countAllProducts(eq(productCode))).thenReturn(totalCount);

        //when
        Page<ProductResponseDto> resultPage = productService.getAllProducts(productCode, pageable);

        //then
        assertAll("Valid pageable scenario",
//                () -> assertThat(resultPage.getContent()).hasSize(3)
//                        .containsExactlyElementsOf(expectedProducts),
                () -> assertThat(resultPage.getTotalElements()).isEqualTo(totalCount),
                () -> assertThat(resultPage.getPageable().getPageNumber()).isEqualTo(pageable.getPageNumber()),
                () -> assertThat(resultPage.getPageable().getPageSize()).isEqualTo(pageable.getPageSize())
        );

    }

    @Test
    void getAllProducts_withInvalidSort() {
        // Given:
        String productCode = null;

        // 잘못된 정렬 조건이 포함된 Pageable 생성 (예: 존재하지 않는 필드 "invalidField")
        Pageable invalidPageable = PageRequest.of(
                0, // 첫번째 페이지
                10,
                Sort.by("invalidField").ascending() // Product 엔티티에 존재하지 않는 필드
        );

        assertThatThrownBy(() -> productService.getAllProducts(productCode, invalidPageable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid sort property");

    }
}
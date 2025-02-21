package com.example.wms.product.application.service;

import com.example.wms.outbound.application.port.out.OutboundPlanProductPort;
import com.example.wms.product.application.domain.Product;
import com.example.wms.product.application.port.out.ProductPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductABCGradeServiceTest {

    @Mock
    private ProductPort productPort;

    @InjectMocks
    private ProductService productService;

    @Mock
    private OutboundPlanProductPort outboundPlanProductPort;

    private List<Product> mockProducts;

    @BeforeEach
    void setUp() {
        mockProducts = Arrays.asList(
                new Product(1L, "a123", "handle", 5000, 10000, 10, 1L, 50, "Electronics", 5, 7, null, null),
                new Product(1L, "a124", "handle2", 6000, 20000, 20, 1L, 50, "Electronics", 5, 7, null, null),
                new Product(1L, "a125", "handle3", 5000, 10000, 10, 1L, 50, "Electronics", 5, 7, null, null)
        );


    }

    @Test
    @DisplayName("품목 별 ABC 등급 부여 테스트")
    void performABCAnalysis() {
        productService.performABCAnalysis();

        verify(outboundPlanProductPort, times(1)).getRequiredQuantitiesPerProduct();
    }

    @Test
    @DisplayName("품목에 bin 코드 부여 테스트")
    void testAssignLocationBinCode() {
        doAnswer(invocation -> {
            Long productId = invocation.getArgument(0);
            String binCode = invocation.getArgument(1);

            mockProducts.stream()
                    .filter(p->p.getProductId().equals(productId))
                    .forEach(p -> p.setLocationBinCode(binCode));

            return null;
        }).when(productPort).updateBinCode(anyLong(), anyString());

        productService.assignLocationBinCode();

        verify(productPort, times(mockProducts.size())).updateBinCode(anyLong(),anyString());
        assertNotNull(mockProducts.get(0).getLocationBinCode(), "bin 코드가 설정되지 않음");
    }
}
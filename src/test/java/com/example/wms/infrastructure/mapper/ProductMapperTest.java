package com.example.wms.infrastructure.mapper;

import com.example.wms.product.application.domain.Product;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.MySQLContainer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductMapperTest {

    // Testcontainers를 사용해 MySQL 컨테이너를 띄움
    @Container
    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    // MySQL 컨테이너에서 생성된 데이터소스 정보를 Spring Boot에 동적으로 주입
    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
    }

    @Autowired
    private ProductMapper productMapper;

//    @Test
//    void findProductWithPagination() {
//        int page = 2;
//        int size = 3;
//        int offset = page * size;
//        String productCode = null;
//
//        Pageable pageable = PageRequest.of(
//                page,
//                size,
//                Sort.by("sale_price").ascending()
//                        .and(Sort.by("purchase_price").descending())
//        );
//
//        // When: 페이지 처리 및 정렬 조건에 맞게 제품 목록 조회
////        List<Product> products = productMapper.findProductWithPagination(productCode, pageable);
//
//        // Then: 반환된 결과가 예상과 일치하는지 검증
//        // (아래 검증 값은 미리 입력된 SQL 데이터가 아래와 같이 삽입되었음을 전제로 합니다.)
//        assertThat(products).hasSize(3);
//
//        // 첫 번째 제품 검증
//        Product p1 = products.get(0);
//        assertAll("First product assertions",
//                () -> assertThat(p1.getProductId()).isEqualTo(14),
//                () -> assertThat(p1.getProductCode()).isEqualTo("P1014"),
//                () -> assertThat(p1.getProductName()).isEqualTo("외장 하드"),
//                () -> assertThat(p1.getPurchasePrice()).isEqualTo(100000),
//                () -> assertThat(p1.getSalePrice()).isEqualTo(140000),
//                () -> assertThat(p1.getLotUnit()).isEqualTo(15),
//                () -> assertThat(p1.getSupplierId()).isEqualTo(14),
//                () -> assertThat(p1.getStockLotCount()).isEqualTo(50),
//                () -> assertThat(p1.getCategory()).isEqualTo("컴퓨터 주변기기"),
//                () -> assertThat(p1.getThreshold()).isEqualTo(5),
//                () -> assertThat(p1.getLeadTime()).isEqualTo(4),
//                () -> assertThat(p1.getLocationBinCode()).isEqualTo("I10"),
//                () -> assertThat(p1.getAbcGrade()).isEqualTo("B")
//        );
//
//        // 두 번째 제품 검증
//        Product p2 = products.get(1);
//        assertAll("Second product assertions",
//                () -> assertThat(p2.getProductId()).isEqualTo(11),
//                () -> assertThat(p2.getProductCode()).isEqualTo("P1011"),
//                () -> assertThat(p2.getProductName()).isEqualTo("블루투스 스피커"),
//                () -> assertThat(p2.getPurchasePrice()).isEqualTo(120000),
//                () -> assertThat(p2.getSalePrice()).isEqualTo(180000),
//                () -> assertThat(p2.getLotUnit()).isEqualTo(8),
//                () -> assertThat(p2.getSupplierId()).isEqualTo(11),
//                () -> assertThat(p2.getStockLotCount()).isEqualTo(20),
//                () -> assertThat(p2.getCategory()).isEqualTo("액세서리"),
//                () -> assertThat(p2.getThreshold()).isEqualTo(4),
//                () -> assertThat(p2.getLeadTime()).isEqualTo(5),
//                () -> assertThat(p2.getLocationBinCode()).isEqualTo("F07"),
//                () -> assertThat(p2.getAbcGrade()).isEqualTo("C")
//        );
//
//        // 세 번째 제품 검증
//        Product p3 = products.get(2);
//        assertAll("Third product assertions",
//                () -> assertThat(p3.getProductId()).isEqualTo(8),
//                () -> assertThat(p3.getProductCode()).isEqualTo("P1008"),
//                () -> assertThat(p3.getProductName()).isEqualTo("헤드폰"),
//                () -> assertThat(p3.getPurchasePrice()).isEqualTo(150000),
//                () -> assertThat(p3.getSalePrice()).isEqualTo(220000),
//                () -> assertThat(p3.getLotUnit()).isEqualTo(10),
//                () -> assertThat(p3.getSupplierId()).isEqualTo(8),
//                () -> assertThat(p3.getStockLotCount()).isEqualTo(35),
//                () -> assertThat(p3.getCategory()).isEqualTo("액세서리"),
//                () -> assertThat(p3.getThreshold()).isEqualTo(5),
//                () -> assertThat(p3.getLeadTime()).isEqualTo(6),
//                () -> assertThat(p3.getLocationBinCode()).isEqualTo("C04"),
//                () -> assertThat(p3.getAbcGrade()).isEqualTo("B")
//        );
//    }

    @Test
    void countAllProducts() {
        String productCode = null;
        // When: 전체 제품 수를 조회
        long totalProducts = productMapper.countAllProducts(productCode);

        // Then: 데이터 SQL에 삽입된 제품 수가 24개라고 가정하면, 총 24가 반환되어야 함
        assertThat(totalProducts).isEqualTo(24);
    }
}
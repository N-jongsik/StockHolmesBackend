package com.example.wms.product.adapter.in;

import com.example.wms.product.adapter.in.dto.ProductOverviewDto;
import com.example.wms.product.adapter.in.dto.ProductResponseDto;
import com.example.wms.product.application.domain.Product;
import com.example.wms.product.application.port.in.ProductUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
@Tag(name = "Product", description = "Product 관련 API")
public class ProductController {

    private final ProductUseCase productUseCase;

    @GetMapping
    @Operation(
            summary = "전체 품목 조회",
            description = "전체 품목 목록을 페이지별로 조회할 수 있습니다.\n" +
            "- **품목 코드 필터링:** 요청 파라미터로 `productCode` 값을 전달하면 해당 문자열이 포함된 품목만 조회합니다.\n" +
            "- **전체 조회:** `productCode`가 null 또는 빈 문자열일 경우 전체 품목을 조회합니다.\n"
    )
    public ResponseEntity<Page<ProductResponseDto>> findAllBinsWithDetails(@RequestParam(required = false) String productCode, @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(productUseCase.getAllProducts(productCode, pageable));
    }

    @GetMapping("/overview")
    @Operation(
            summary = "품목 요약 조회",
            description = "전체 품목 목록의 일부 컬럼(productId, productCode, productName)만 조회합니다."
    )
    public ResponseEntity<List<ProductOverviewDto>> findProductOverview() {
        List<ProductOverviewDto> overviewList = productUseCase.getProductOverview();
        return ResponseEntity.ok(overviewList);
    }


}

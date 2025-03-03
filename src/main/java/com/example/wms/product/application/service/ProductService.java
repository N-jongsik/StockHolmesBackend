package com.example.wms.product.application.service;

import com.example.wms.infrastructure.pagination.util.PageableUtils;
import com.example.wms.outbound.adapter.in.dto.ABCAnalysisDataDto;
import com.example.wms.outbound.application.port.out.OutboundPlanProductPort;
import com.example.wms.product.adapter.in.dto.ProductOverviewDto;
import com.example.wms.product.adapter.in.dto.ProductResponseDto;
import com.example.wms.product.application.domain.Product;
import com.example.wms.product.application.port.in.ProductUseCase;
import com.example.wms.product.application.port.out.ProductPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class ProductService implements ProductUseCase {

    private final ProductPort productPort;

    private final OutboundPlanProductPort outboundPlanProductPort;


    @Override
    public void performABCAnalysis() {

        List<ABCAnalysisDataDto> products = outboundPlanProductPort.getRequiredQuantitiesPerProduct();

        if (products.isEmpty()) {
            return;
        }

        List<ABCAnalysisDataDto> sortedData = products.stream()
                .sorted((d1, d2) -> Integer.compare(d2.getTotalRequiredQuantity(), d1.getTotalRequiredQuantity()))
                .collect(Collectors.toList());


        int totalRequiredQuantity = sortedData.stream()
                .mapToInt(ABCAnalysisDataDto::getTotalRequiredQuantity)
                .sum();

        int cumulativeQuantity = 0;

        for (ABCAnalysisDataDto data : sortedData) {
            cumulativeQuantity += data.getTotalRequiredQuantity();
            double percentage = ((double) cumulativeQuantity / totalRequiredQuantity) * 100;

            String abcGrade;
            if (percentage <= 70) {
                abcGrade = "A";
            } else if (percentage <= 90) {
                abcGrade = "B";
            } else {
                abcGrade = "C";
            }

            productPort.updateABCGrades(data.getProductId(), abcGrade); // 품목별로 ABC 등급 매기는 것 까지 완료

        }
    }

    @Override
    public void assignLocationBinCode() {
        List<Product> products = productPort.getAllProducts();
        if (products.isEmpty()) {
            return;
        }
        
        // 재고 로트 수량을 고려하여 내림차순 정렬 (많은 재고부터 배치)
        products.sort((p1,p2) -> Integer.compare(p2.getStockLotCount(), p1.getStockLotCount()));

        assignBins(products);
    }

    private void assignBins(List<Product> products) {
        int indexA = 0, indexB = 0, indexC = 0;

        for (Product product : products) {
            String abcGrade = product.getAbcGrade();
            String zone;
            int index = 0;

            if ("A".equals(abcGrade)) {
                zone = getZone("A", indexA);
                index = indexA++;
            } else if ("B".equals(abcGrade)) {
                zone = getZone("B",indexB);
                index = indexB++;
            } else {
                zone = "F";
                index = indexC++;
            }

            String aisle = String.format("%02d", (index / 36 % 6 ) + 1);
            String row = String.format("%02d", (index / 6 % 6) + 1);
            String floor = String.format("%02d", (index % 6) + 1);
            int stockLotCount = product.getStockLotCount();

            String binCode;

            if (stockLotCount <= 6) {
                binCode = String.format("%s-%s-%s-%s", zone, aisle, row, floor); // A-01-01-01
            } else if (stockLotCount <= 36) {
                binCode = String.format("%s-%s-%s", zone, aisle, row); // A-01-01
            } else if (stockLotCount <= 216) {
                binCode = String.format("%s-%s", zone, aisle);
            } else {
                binCode = zone;
            }

            productPort.updateBinCode(product.getProductId(), binCode);
        }
    }

    private String getZone(String grade, int index) {
        switch (grade) {
            case "A":
                return switch ((index / 216) % 3) {
                    case 0 -> "A";
                    case 1 -> "B";
                    default -> "C";
                };
            case "B":
                return (index / 216) % 2 == 0 ? "D" : "E";
            default:
                return "F";
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getAllProducts(String productCode, Pageable pageable) {
        // 외부에서 받은 Pageable을 안전하게 변환 (유효하지 않은 정렬 조건이 있으면 예외 발생)
        Pageable safePageable = PageableUtils.convertToSafePageableStrict(pageable, ProductResponseDto.class);

        // safePageable을 Mapper에 전달해서 DB 쿼리를 실행
        // (여기서는 Mapper의 반환타입이나 사용법에 따라 적절히 변환)
        List<ProductResponseDto> productList = productPort.findProductWithPagination(productCode, safePageable);
        long count = productPort.countAllProducts(productCode);

        // PageImpl을 사용하여 Page 객체로 감싸서 반환하는 예시
        return new PageImpl<>(productList, safePageable, count);
    }

    @Override
    public List<ProductOverviewDto> getProductOverview() {
        return productPort.findProductOverview();
    }
}

package com.example.wms.testdata.application.service;

import com.example.wms.infrastructure.mapper.TestDataMapper;
import com.example.wms.order.application.domain.Supplier;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.domain.OutboundPlanProduct;
import com.example.wms.product.application.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TestDataGenerator {

    private final TestDataMapper testDataMapper;

    public TestDataGenerator(TestDataMapper testDataMapper) {
        this.testDataMapper = testDataMapper;
    }

    @Transactional
    public void insertTestData() {
        LocalDate today = LocalDate.now();

        List<Supplier> supplierList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Supplier supplier = Supplier.builder()
                    .supplierName("Supplier " + i)
                    .businessNumber("BN" + i)
                    .representativeName("Rep " + i)
                    .address("Address " + i)
                    .supplierPhone("010-0000-" + String.format("%04d", i))
                    .managerPhone("010-1111-" + String.format("%04d", i))
                    .build();
            testDataMapper.insertSupplier(supplier); // insertSupplier 메서드를 추가해야 함
            supplierList.add(supplier);
        }

        // 1. Product 100건 개별 삽입 (생성된 키 회수)
        List<Product> productList = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            Product product = Product.builder()
                    .productCode(String.format("P%03d", i))
                    .productName("Product " + i)
                    .purchasePrice(100 + i)
                    .salePrice(200 + i)
                    .lotUnit(10)
                    .supplierId((long) (i % 10 + 1)) // supplierId: 1 ~ 10
                    .stockLotCount(5)
                    .category("CategoryA")
                    .threshold(2)
                    .leadTime(3)
                    .locationBinCode(String.format("BIN%03d", i))
                    .abcGrade("A")
                    .build();
            // 개별 insert로 생성된 키를 product 객체에 설정 (Mapper의 @Options(useGeneratedKeys=true) 필요)
            testDataMapper.insertProduct(product);
            productList.add(product);
        }

        // 2. OutboundPlan 80,000건 개별 삽입 (생성된 키 회수)
        List<OutboundPlan> outboundPlanList = new ArrayList<>();
        for (int i = 1; i <= 80000; i++) {
            OutboundPlan outboundPlan = OutboundPlan.builder()
                    .planDate(today)
                    .status("대기")
                    .outboundScheduleNumber(String.format("OSN%05d", i))
                    .outboundScheduleDate(today)
                    .productionPlanNumber(String.format("PPN%05d", i))
                    .build();
            // 개별 insert로 생성된 키 회수 (Mapper의 @Options(useGeneratedKeys=true) 필요)
            testDataMapper.insertOutboundPlan(outboundPlan);
            outboundPlanList.add(outboundPlan);
        }

        // 3. OutboundPlanProduct 100,000건 배치 삽입 (실제 키 사용)
        List<OutboundPlanProduct> planProductList = new ArrayList<>();
        for (int i = 1; i <= 100000; i++) {
            // 제품은 100건, 출고계획은 80,000건에서 (i-1) modulo로 순환
            Long productId = productList.get((i - 1) % productList.size()).getProductId();
            Long outboundPlanId = outboundPlanList.get((i - 1) % outboundPlanList.size()).getOutboundPlanId();
            OutboundPlanProduct planProduct = OutboundPlanProduct.builder()
                    .productId(productId)
                    .outboundPlanId(outboundPlanId)
                    .requiredQuantity(10 + (i % 100))
                    .stockUsedQuantity(5 + (i % 100))
                    .orderQuantity(8 + (i % 100))
                    .status("대기")
                    .build();
            planProductList.add(planProduct);
        }
        testDataMapper.batchInsertOutboundPlanProduct(planProductList);

// 4. Outbound 총 50,000건 배치 삽입
//    그룹별로 다음과 같이 생성:
//    - 지시(OutboundAssign): 10,000건 (본인 단계 번호만)
//    - 피킹(OutboundPicking): 10,000건 (지시번호 + 피킹번호)
//    - 패킹(OutboundPacking): 10,000건 (지시번호 + 피킹번호 + 패킹번호)
//    - 로딩(OutboundLoading): 20,000건 (지시번호 + 피킹번호 + 패킹번호 + 로딩번호)
        List<Outbound> outboundList = new ArrayList<>();

// Group 1: 지시 10,000건
        for (int i = 1; i <= 10000; i++) {
            int idx = (i - 1) % outboundPlanList.size();
            OutboundPlan op = outboundPlanList.get(idx);
            Outbound outbound = Outbound.builder()
                    .outboundPlanId(op.getOutboundPlanId())
                    .outboundAssignNumber(String.format("OAN%05d", i))
                    .outboundAssignDate(today)
                    .build();
            outboundList.add(outbound);
        }
// Group 2: 피킹 10,000건 (지시번호 + 피킹번호)
        for (int i = 1; i <= 10000; i++) {
            int idx = (i - 1) % outboundPlanList.size();
            OutboundPlan op = outboundPlanList.get(idx);
            Outbound outbound = Outbound.builder()
                    .outboundPlanId(op.getOutboundPlanId())
                    // 이전 단계: 지시
                    .outboundAssignNumber(String.format("OAN%05d", i))
                    .outboundAssignDate(today)
                    // 본인 단계: 피킹
                    .outboundPickingNumber(String.format("OPN%05d", i))
                    .outboundPickingDate(today)
                    .build();
            outboundList.add(outbound);
        }
// Group 3: 패킹 10,000건 (지시번호 + 피킹번호 + 패킹번호)
        for (int i = 1; i <= 10000; i++) {
            int idx = (i - 1) % outboundPlanList.size();
            OutboundPlan op = outboundPlanList.get(idx);
            Outbound outbound = Outbound.builder()
                    .outboundPlanId(op.getOutboundPlanId())
                    // 이전 단계: 지시, 피킹
                    .outboundAssignNumber(String.format("OAN%05d", i))
                    .outboundAssignDate(today)
                    .outboundPickingNumber(String.format("OPN%05d", i))
                    .outboundPickingDate(today)
                    // 본인 단계: 패킹
                    .outboundPackingNumber(String.format("OPaN%05d", i))
                    .outboundPackingDate(today)
                    .build();
            outboundList.add(outbound);
        }
// Group 4: 로딩 20,000건 (지시번호 + 피킹번호 + 패킹번호 + 로딩번호)
        for (int i = 1; i <= 20000; i++) {
            int idx = (i - 1) % outboundPlanList.size();
            OutboundPlan op = outboundPlanList.get(idx);
            Outbound outbound = Outbound.builder()
                    .outboundPlanId(op.getOutboundPlanId())
                    // 이전 단계: 지시, 피킹, 패킹
                    .outboundAssignNumber(String.format("OAN%05d", i))
                    .outboundAssignDate(today)
                    .outboundPickingNumber(String.format("OPN%05d", i))
                    .outboundPickingDate(today)
                    .outboundPackingNumber(String.format("OPaN%05d", i))
                    .outboundPackingDate(today)
                    // 본인 단계: 로딩
                    .outboundLoadingNumber(String.format("OLN%05d", i))
                    .outboundLoadingDate(today)
                    .build();
            outboundList.add(outbound);
        }
        testDataMapper.batchInsertOutbound(outboundList);
    }
}

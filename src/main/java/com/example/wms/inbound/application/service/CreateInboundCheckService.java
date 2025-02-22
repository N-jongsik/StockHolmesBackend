package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.request.InboundCheckReqDto;
import com.example.wms.inbound.adapter.in.dto.request.InboundCheckedProductReqDto;
import com.example.wms.inbound.application.domain.Inbound;
import com.example.wms.inbound.application.port.in.CreateInboundCheckUseCase;
import com.example.wms.inbound.application.port.out.AssignInboundNumberPort;
import com.example.wms.inbound.application.port.out.InboundPort;
import com.example.wms.infrastructure.exception.NotFoundException;
import com.example.wms.inventory.application.port.out.InventoryPort;
import com.example.wms.order.application.domain.OrderProduct;
import com.example.wms.order.application.port.out.OrderPort;
import com.example.wms.order.application.port.out.OrderProductPort;
import com.example.wms.product.application.domain.Lot;
import com.example.wms.product.application.domain.LotStatus;
import com.example.wms.product.application.domain.Product;
import com.example.wms.product.application.port.in.BinUseCase;
import com.example.wms.product.application.port.out.LotPort;
import com.example.wms.product.application.port.out.ProductPort;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class CreateInboundCheckService implements CreateInboundCheckUseCase {

    private final InboundPort inboundPort;
    private final ProductPort productPort;
    private final AssignInboundNumberPort assignInboundNumberPort;
    private final OrderPort orderPort;
    private final OrderProductPort orderProductPort;
    private final BinUseCase binUseCase;
    private final LotPort lotPort;
    private final InventoryPort inventoryPort;


    @Transactional
    @Override
    public void createInboundCheck(Long inboundId, InboundCheckReqDto inboundCheckReqDto) { // 품목별 검사 개수

        Inbound inbound = inboundPort.findById(inboundId);

        if (inbound == null) {
            throw new NotFoundException("inbound not found with id " + inboundId);
        }

        Map<Long, List<DefectiveProductInfo>> supplierToDefectiveProducts = new HashMap<>();

        for (InboundCheckedProductReqDto checkedProduct : inboundCheckReqDto.getCheckedProductList()) {

            Long productId = checkedProduct.getProductId();
            Integer defectiveCount = checkedProduct.getDefectiveCount().intValue();
            Product product = productPort.findById(productId);

            if (product == null) {
                throw new NotFoundException("product not found with id : " + productId);
            }

            OrderProduct existingOrderProduct = orderProductPort.findByOrderId(inbound.getOrderId(), productId);
            orderProductPort.update(existingOrderProduct.getOrderProductId(), (long) defectiveCount);
            int putAwayCount = (existingOrderProduct.getProductCount() - defectiveCount) / product.getLotUnit();
            handleLotCreation(productId, inboundId, putAwayCount);

            if (defectiveCount > 0) {
                Long supplierId = product.getSupplierId();
                DefectiveProductInfo defectiveInfo = new DefectiveProductInfo(
                        productId,
                        defectiveCount,
                        product.getProductName()
                );

                supplierToDefectiveProducts.computeIfAbsent(supplierId, k -> new ArrayList<>())
                        .add(defectiveInfo);
            }
        }

        for (Map.Entry<Long, List<DefectiveProductInfo>> entry : supplierToDefectiveProducts.entrySet()) {
            Long supplierId = entry.getKey();
            List<DefectiveProductInfo> defectiveProducts = entry.getValue();

            Long orderId = orderPort.createOrderWithSupplier(supplierId, inboundId);

            for (DefectiveProductInfo defectiveInfo : defectiveProducts) {
                OrderProduct newOrderProduct = OrderProduct.builder()
                        .orderId(orderId)
                        .productCount(defectiveInfo.defectiveCount)
                        .productId(defectiveInfo.productId)
                        .productName(defectiveInfo.productName)
                        .isDefective(true)
                        .defectiveCount(0L)
                        .build();
                orderProductPort.save(newOrderProduct);
            }
        }

        inboundPort.updateIC(inbound.getInboundId(), LocalDate.now(), makeNumber("IC"), "입하검사");

    }

    @Getter
    @AllArgsConstructor
    private static class DefectiveProductInfo {
        private Long productId;
        private Integer defectiveCount;
        private String productName;
    }

    private void handleLotCreation(Long productId, Long inboundId, int putAwayCount) {
        String locationBinCode = productPort.getLocationBinCode(productId);
        List<Long> binIds = binUseCase.assignBinIdsToLots(locationBinCode, putAwayCount);

        if (binIds.size() < putAwayCount) {
            createLotsWithAssignedBins(productId, inboundId, binIds);
            createLotsWithDefaultBins(productId, inboundId, putAwayCount - binIds.size());
        } else {
            createLotsWithAssignedBins(productId, inboundId, binIds.subList(0, putAwayCount));
        }
        inventoryPort.updateInventory(productId, putAwayCount);
        productPort.updateRequiredQuantity(productId, putAwayCount);
    }

    private void createLotsWithAssignedBins(Long productId, Long inboundId, List<Long> binIds) {
        for (Long binId : binIds) {
            Lot lot = Lot.builder()
                    .productId(productId)
                    .binId(binId)
                    .lotNumber(makeNumber("LO"))
                    .status(LotStatus.입고)
                    .inboundId(inboundId)
                    .build();
            lotPort.insertLot(lot);
        }
    }

    private void createLotsWithDefaultBins(Long productId, Long inboundId, int count) {
        for (int i = 0; i < count; i++) {
            Lot lot = Lot.builder()
                    .productId(productId)
                    .binId(100L + i)
                    .lotNumber(makeNumber("LO"))
                    .status(LotStatus.입고)
                    .inboundId(inboundId)
                    .build();
            lotPort.insertLot(lot);
        }


    }

    private String makeNumber(String format) {
        String currentDate = LocalDate.now().toString().replace("-","");
        String number = switch (format) {
            case "LO" -> assignInboundNumberPort.findMaxLONumber();
            default -> null;
        };

        String nextNumber = "0000";

        if (number != null) {
            int lastNumber = Integer.parseInt(number.substring(number.length()-4));
            nextNumber = String.format("%04d", lastNumber+1);
        }

        return format + currentDate + nextNumber;
    }
}

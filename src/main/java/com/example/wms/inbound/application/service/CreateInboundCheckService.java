package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.request.InboundCheckReqDto;
import com.example.wms.inbound.adapter.in.dto.request.InboundCheckedProductReqDto;
import com.example.wms.inbound.adapter.in.dto.request.InboundPutAwayReqDto;
import com.example.wms.inbound.application.domain.Inbound;
import com.example.wms.inbound.application.port.in.CreateInboundCheckUseCase;
import com.example.wms.inbound.application.port.out.AssignInboundNumberPort;
import com.example.wms.inbound.application.port.out.InboundPort;
import com.example.wms.infrastructure.exception.NotFoundException;
import com.example.wms.order.application.domain.OrderProduct;
import com.example.wms.order.application.port.out.OrderPort;
import com.example.wms.order.application.port.out.OrderProductPort;
import com.example.wms.product.application.domain.Lot;
import com.example.wms.product.application.domain.LotStatus;
import com.example.wms.product.application.domain.Product;
import com.example.wms.product.application.port.in.BinUseCase;
import com.example.wms.product.application.port.out.LotPort;
import com.example.wms.product.application.port.out.ProductPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


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

    @Transactional
    @Override
    public void createInboundCheck(Long inboundId, InboundCheckReqDto inboundCheckReqDto) {

        Inbound inbound = inboundPort.findById(inboundId);

        if (inbound == null) {
            throw new NotFoundException("inbound not found with id " + inboundId);
        }

        for (InboundCheckedProductReqDto checkedProduct : inboundCheckReqDto.getCheckedProductList()) {

            Long productId = checkedProduct.getProductId();
            Integer count = (checkedProduct.getDefectiveCount().intValue());

            int defectiveCount = count / productPort.findById(productId).getLotUnit();
            Product product = productPort.findById(productId);

            if (product == null) {
                throw new NotFoundException("product not found with id :" + productId);
            }

            long countLongValue = defectiveCount;
            if (defectiveCount > 0) {
                Long orderId = orderPort.createOrder(productId, inboundId, countLongValue);
                OrderProduct orderProduct = OrderProduct.builder()
                        .orderId(orderId)
                        .productCount(defectiveCount*product.getLotUnit())
                        .productId(productId)
                        .productName(product.getProductName())
                        .isDefective(true)
                        .defectiveCount((long)defectiveCount)
                        .build();
                orderProductPort.save(orderProduct);
            }

            List<InboundPutAwayReqDto> putAwayRequests = productPort.findPutAwayProductsByInboundId(inboundId)
                    .stream()
                    .map(p -> InboundPutAwayReqDto.builder()
                            .productId(p.getProductId())
                            .lotCount(countLongValue)
                            .build())
                    .collect(Collectors.toList());

            for (InboundPutAwayReqDto request : putAwayRequests) {
                Integer lotCount = request.getLotCount().intValue();
                String locationBinCode = productPort.getLocationBinCode(request.getProductId());

                List<Long> binIds = binUseCase.assignBinIdsToLots(locationBinCode, lotCount);

                // bin의 amount가 넣으려는 lot 개수보다 부족할 경우
                if (binIds.size() < lotCount) {
                    for (int i = 0; i< binIds.size(); i++) {
                        String lotNumber = makeNumber("LO");

                        Lot lot = Lot.builder()
                                .productId(productId)
                                .binId(binIds.get(i))
                                .lotNumber(lotNumber)
                                .status(LotStatus.입고)
                                .inboundId(inboundId)
                                .build();
                        lotPort.insertLot(lot);

                    }
                }

                else {
                    for (int i = 0; i < lotCount; i++) {
                        String lotNumber = makeNumber("LO");

                        Lot lot = Lot.builder()
                                .productId(productId)
                                .binId(binIds.get(i))
                                .lotNumber(lotNumber)
                                .status(LotStatus.입고)
                                .inboundId(inboundId)
                                .build();
                        lotPort.insertLot(lot);
                    }
                }

            }
            inboundPort.updateIC(inbound.getInboundId(), LocalDate.now(), makeNumber("IC"), "입하검사");
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

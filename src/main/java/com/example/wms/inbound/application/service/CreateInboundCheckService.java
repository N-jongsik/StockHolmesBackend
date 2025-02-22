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
    public void createInboundCheck(Long inboundId, InboundCheckReqDto inboundCheckReqDto) { // 품목별 검사 개수

        Inbound inbound = inboundPort.findById(inboundId);

        if (inbound == null) {
            throw new NotFoundException("inbound not found with id " + inboundId);
        }

                    for (InboundCheckedProductReqDto checkedProduct : inboundCheckReqDto.getCheckedProductList()) { // 품목별 검사
                        // productId랑 defectiveCount(40개 불량) 을 가져옴


                        Long productId = checkedProduct.getProductId(); // productId
                        Integer count = (checkedProduct.getDefectiveCount().intValue()); // defectiveCount 를 가져옴
                        Product product = productPort.findById(productId); // product 객체를 가져옴

                        int defectiveCount = count / product.getLotUnit();

                        if (product == null) {
                            throw new NotFoundException("product not found with id :" + productId); // 없으면 처리
                        }

                        long countLongValue = defectiveCount; // 불량인 단위 개수를 long으로 바꿈

                        if (defectiveCount > 0) { // 재발주
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

                        // 적치
                        List<InboundPutAwayReqDto> putAwayRequests = productPort.findPutAwayProductsByInboundId(inboundId)
                                .stream()
                                .map(p -> InboundPutAwayReqDto.builder()
                                        .productId(p.getProductId())
                                        .lotCount(countLongValue) // lot 개수
                                        .build())
                                .collect(Collectors.toList());

                        for (InboundPutAwayReqDto request : putAwayRequests) { // 현재 입하 테이블에 있는 품목 개수
                            Integer lotCount = request.getLotCount().intValue();
                            String locationBinCode = productPort.getLocationBinCode(request.getProductId());

                            OrderProduct orderProduct = orderProductPort.findByOrderId(inbound.getOrderId(), productId);
                            int putAwayCount = (orderProduct.getProductCount() - count) / product.getLotUnit();
                            // order 이미 존재하는 Order를 찾고 order_product 찾고 - count 값을 불러오면 - defective
                            List<Long> binIds = binUseCase.assignBinIdsToLots(locationBinCode, lotCount);



                            // lot 생성할 때 order_product 의 count 값이랑 - defective 를 뺀 값만큼 Lot를 생성  / lot _unit -> lotCount 값으로 업데이트
                            // bin의 amount가 넣으려는 lot 개수보다 부족할 경우
                            if (binIds.size() < putAwayCount) {
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
                                for (int i = 0; i < putAwayCount; i++) {
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

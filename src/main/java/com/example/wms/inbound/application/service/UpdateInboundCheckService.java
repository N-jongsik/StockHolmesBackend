package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.request.InboundCheckUpdateReqDto;
import com.example.wms.inbound.adapter.in.dto.request.InboundCheckedProductReqDto;
import com.example.wms.inbound.application.domain.Inbound;
import com.example.wms.inbound.application.port.in.UpdateInboundCheckUseCase;
import com.example.wms.inbound.application.port.out.AssignInboundNumberPort;
import com.example.wms.inbound.application.port.out.InboundPort;
import com.example.wms.infrastructure.exception.NotFoundException;
import com.example.wms.order.application.domain.OrderProduct;
import com.example.wms.order.application.port.out.OrderPort;
import com.example.wms.order.application.port.out.OrderProductPort;
import com.example.wms.product.application.domain.Product;
import com.example.wms.product.application.port.out.ProductPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UpdateInboundCheckService implements UpdateInboundCheckUseCase {

    private final InboundPort inboundPort;

    private final OrderProductPort orderProductPort;
    private final ProductPort productPort;
    private final OrderPort orderPort;
    private final AssignInboundNumberPort assignInboundNumberPort;

    private String makeNumber(String format) {
        String currentDate = LocalDate.now().toString().replace("-","");
        String number = switch (format) {
            case "IS" -> assignInboundNumberPort.findMaxISNumber();
            case "IC" -> assignInboundNumberPort.findMaxICNumber();
            case "PA" -> assignInboundNumberPort.findMaxPANumber();
            default -> null;
        };

        String nextNumber = "0000";

        if (number != null) {
            int lastNumber = Integer.parseInt(number.substring(number.length()-4));
            nextNumber = String.format("%04d", lastNumber+1);
        }

        return format + currentDate + nextNumber;
    }



    @Transactional
    @Override
    public void updateInboundCheck(Long inboundId, InboundCheckUpdateReqDto updateReqDto) {

        Inbound inbound = inboundPort.findById(inboundId);

        if (inbound == null) {
            throw new NotFoundException("Inbound not found with id " + inboundId);
        }

        inbound.setCheckDate(LocalDate.now());
        inbound.setCheckNumber(makeNumber("IC"));
        inboundPort.updateIC(inbound.getInboundId(), inbound.getCheckDate(), makeNumber("IC"), "입하검사"); // 관련 inbound 테이블에 업데이트

        for (InboundCheckedProductReqDto checkedProduct : updateReqDto.getCheckedProductList()) { // 품목별 defectiveCount
            OrderProduct orderProduct = orderProductPort.findByProductId(checkedProduct.getProductId());

            if (orderProduct == null) {
                throw new NotFoundException("OrderProduct not found with productId : " + checkedProduct.getProductId());
            }

            Long beforeDefectiveCount = orderProduct.getDefectiveCount(); // 기존 defectiveCount

            Long productId = checkedProduct.getProductId();

            Long updatedDefectiveCount = checkedProduct.getDefectiveCount(); // 수정한 defectiveCount

            Product product = productPort.findById(productId);


            if (product == null) {
                throw new NotFoundException("Product not found with id : " + productId);
            }

            if (beforeDefectiveCount - updatedDefectiveCount < 0) {
                orderPort.createOrder(productId, inboundId, updatedDefectiveCount-beforeDefectiveCount); // 재발주
            } else if (beforeDefectiveCount - updatedDefectiveCount > 0) {
                // 발주 수정 메서드 추가
                orderProduct.setDefectiveCount(updatedDefectiveCount);
            }
            orderProduct.setDefectiveCount(updatedDefectiveCount); // 발주 품목 별 불량품 개수 업데이트
        }

    }

}

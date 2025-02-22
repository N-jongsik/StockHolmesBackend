package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.request.InboundPutAwayReqDto;
import com.example.wms.inbound.application.domain.Inbound;
import com.example.wms.inbound.application.port.in.CreateInboundPutAwayUseCase;
import com.example.wms.inbound.application.port.out.AssignInboundNumberPort;
import com.example.wms.inbound.application.port.out.InboundPort;
import com.example.wms.infrastructure.exception.NotFoundException;
import com.example.wms.inventory.application.port.out.InventoryPort;
import com.example.wms.product.application.domain.Product;
import com.example.wms.product.application.port.out.ProductPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreateInboundPutAwayService implements CreateInboundPutAwayUseCase {

    private final InboundPort inboundPort;
    private final ProductPort productPort;
    private final InventoryPort inventoryPort;
    private final AssignInboundNumberPort assignInboundNumberPort;

    @Override
    public void createPutAway(Long inboundId) {

        Inbound inbound = inboundPort.findById(inboundId); // inbound 찾음

        if (inbound == null) {
            throw new NotFoundException("Inbound not found with id " + inboundId);
        }

        String putAwayNumber = makeNumber("PA");
        LocalDate putAwayDate = LocalDate.now();
        String inboundStatus = "입고적치";

        inboundPort.updatePA(inboundId, putAwayDate, putAwayNumber, inboundStatus); // inbound 업데이트

        List<InboundPutAwayReqDto> putAwayRequests = productPort.findPutAwayProductsByInboundId(inboundId)
                .stream()
                .map(product -> InboundPutAwayReqDto.builder()
                        .productId(product.getProductId())
                        .lotCount(Long.valueOf(product.getStockLotCount()))
                        .build())
                .collect(Collectors.toList());

        for (InboundPutAwayReqDto request : putAwayRequests) {
            Long productId = request.getProductId();
            Long lotCount = request.getLotCount();

            Product product = productPort.findById(productId);
            Integer lotUnit = product.getLotUnit();
            long totalCount = lotCount * lotUnit;
            inventoryPort.updateInventory(productId, (int)totalCount);
            productPort.updateRequiredQuantity(productId, lotCount.intValue());
        }
    }

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


}

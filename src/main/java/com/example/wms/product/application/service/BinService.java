package com.example.wms.product.application.service;

import com.example.wms.product.adapter.in.dto.BinResponseDto;
import com.example.wms.product.adapter.in.dto.LotInBinDto;
import com.example.wms.product.adapter.in.dto.ProductInBinDto;
import com.example.wms.product.adapter.out.dto.FlatBinDto;
import com.example.wms.product.application.port.in.BinUseCase;
import com.example.wms.product.application.port.out.BinPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BinService implements BinUseCase {

    private final BinPort binPort;
    @Override
    public List<BinResponseDto> findAllBinsWithDetails() {
        List<FlatBinDto> flatBinDtos = binPort.selectAllBinsWithDetails();
        return convertFlatToHierarchical(flatBinDtos);
    }


    private List<BinResponseDto> convertFlatToHierarchical(List<FlatBinDto> flatBinDtos) {
        Map<Long, BinResponseDto> binMap = new HashMap<>();

        for (FlatBinDto flat : flatBinDtos) {
            // BinResponseDto가 없으면 새로 추가
            binMap.computeIfAbsent(flat.getBinId(), id -> BinResponseDto.builder()
                    .binId(flat.getBinId())
                    .binCode(flat.getBinCode())
                    .zone(flat.getZone())
                    .aisle(flat.getAisle())
                    .row(flat.getRow())
                    .floor(flat.getFloor())
                    .amount(flat.getAmount())
                    .lotList(new ArrayList<>())  // ✅ 빈 리스트 생성
                    .build()
            );

            BinResponseDto bin = binMap.get(flat.getBinId());

            if (flat.getLotId() == null) {
                continue;
            }

            // Lot 객체 생성
            LotInBinDto lot = LotInBinDto.builder()
                    .lotId(flat.getLotId())
                    .lotNumber(flat.getLotNumber())
                    .productId(flat.getProductId())
                    .binId(flat.getBinId())
                    .status(flat.getStatus())
                    .inboundId(flat.getInboundId())
                    .outboundId(flat.getOutboundId())
                    .productInBinDto(
                            flat.getProductId() != null ? ProductInBinDto.builder()
                                    .productCode(flat.getProductCode())
                                    .productName(flat.getProductName())
                                    .build()
                                    : null // Product 데이터가 없을 경우 null 처리
                    )
                    .build();

            // bin에 lot 추가 (중복 방지)
            if (!bin.getLotList().contains(lot)) {
                bin.getLotList().add(lot);
            }
        }

        return new ArrayList<>(binMap.values());
    }

    @Override
    public List<Long> assignBinIdsToLots(String locationBinCode, int lotCount) {
        List<Long> binIds;

        if (locationBinCode.split("-").length == 4) {
            Long binId = binPort.findExactBinIdByBinCode(locationBinCode);
            binIds = (binId != null) ? List.of(binId) : List.of();
        } else {
            binIds = binPort.findBinIdsByBinPrefix(locationBinCode);
        }

        return binIds.size() >= lotCount ? binIds.subList(0, lotCount) : binIds;
    }
}

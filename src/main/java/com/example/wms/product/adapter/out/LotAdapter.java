package com.example.wms.product.adapter.out;

import com.example.wms.infrastructure.mapper.LotMapper;
import com.example.wms.product.application.domain.Lot;
import com.example.wms.product.application.port.out.LotPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LotAdapter implements LotPort {

    private final LotMapper lotMapper;

    @Override
    public void updateStatus(Long lotId, String status) {
        status = "입고";
        lotMapper.updateStatus(lotId, status);
    }

    @Override
    public void insertLot(Lot lot) {
        lotMapper.save(lot);
    }

    @Override
    public List<Lot> findLotsByProductId(Long productId, int requiredLotCount) {
        return lotMapper.findLotsByProductId(productId, requiredLotCount);
    }

    @Override
    public List<Lot> findLotsSupplierByProductId(Long productId, int requiredLotCount) {
        return lotMapper.findLotsSupplierByProductId(productId, requiredLotCount);
    }

    @Override
    public void updateOutboundIdForLots(List<Long> lotIds, Long outboundId) {
        lotMapper.updateOutboundIdForLots(lotIds, outboundId);
    }

    @Override
    public List<Lot> findLotsByProductIdAndCreateDate(Long productId, int requiredLotCount, LocalDate createDate) {
        return List.of();
    }
}

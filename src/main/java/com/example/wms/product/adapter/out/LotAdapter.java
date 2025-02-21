package com.example.wms.product.adapter.out;

import com.example.wms.infrastructure.mapper.LotMapper;
import com.example.wms.product.application.domain.Lot;
import com.example.wms.product.application.port.out.LotPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
    public Long insertLot(Lot lot) {
        return lotMapper.save(lot);
    }

    @Override
    public Long findLot(Long binId) {
        return lotMapper.findLot(binId);
    }

    @Override
    public Lot findById(Long lotId) {
        return lotMapper.findById(lotId);
    }
}

package com.example.wms.product.application.port.out;

import com.example.wms.product.application.domain.Lot;

public interface LotPort {
    void updateStatus(Long lotId, String status);
    Long insertLot(Lot lot);
    Long findLot(Long binId);
    Lot findById(Long lotId);
}

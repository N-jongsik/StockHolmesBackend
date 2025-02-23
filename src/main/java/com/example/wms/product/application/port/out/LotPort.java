package com.example.wms.product.application.port.out;

import com.example.wms.product.application.domain.Lot;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

public interface LotPort {
    void updateStatus(Long lotId, String status);
    List<Lot> findLotsByProductId(Long productId, int requiredLotCount);
    List<Lot> findLotsSupplierByProductId(Long productId, int requiredLotCount);

    void updateOutboundIdForLots(List<Long> lotIds, Long outboundId);

    List<Lot> findLotsByProductIdAndCreateDate(Long productId, int requiredLotCount, LocalDate createDate);
    Long insertLot(Lot lot);
    Long findLot(Long binId);
    Lot findById(Long lotId);
}

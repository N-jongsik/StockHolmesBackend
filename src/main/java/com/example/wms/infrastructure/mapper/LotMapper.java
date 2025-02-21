package com.example.wms.infrastructure.mapper;

import com.example.wms.product.application.domain.Lot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LotMapper {
    void updateStatus(Long lotId, String status);
    void save(Lot lot);
    List<Lot> findLotsByProductId(@Param("productId") Long productId,
                                  @Param("requiredLotCount") int requiredLotCount);

    List<Lot> findLotsSupplierByProductId(@Param("productId") Long productId,
                                  @Param("requiredLotCount") int requiredLotCount);

    void updateOutboundIdForLots(@Param("lotIds") List<Long> lotIds,
                                 @Param("outboundId") Long outboundId);

}

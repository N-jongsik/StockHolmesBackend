package com.example.wms.product.application.port.in;

import com.example.wms.product.adapter.in.dto.BinResponseDto;

import java.util.List;

public interface BinUseCase {
    List<BinResponseDto> findAllBinsWithDetails();
    List<Long> assignBinIdsToLots(String locationBinCode, int lotCount);
}

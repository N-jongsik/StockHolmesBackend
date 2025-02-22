package com.example.wms.product.adapter.out;

import com.example.wms.infrastructure.mapper.BinMapper;
import com.example.wms.product.adapter.out.dto.FlatBinDto;
import com.example.wms.product.application.domain.Bin;
import com.example.wms.product.application.port.out.BinPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BinAdapter implements BinPort {
    private final BinMapper binMapper;

    @Override
    public List<FlatBinDto> selectAllBinsWithDetails() {
        return binMapper.selectAllBinsWithDetails();
    }

    @Override
    public Long findAvailableBinIdInAisle(String zone, Integer aisle) {
        return binMapper.findAvailableBinIdInAisle(zone, aisle);
    }

    @Override
    public Long findBinIdByBinCode(String locationBinCode) {
        return binMapper.findBinIdByBinCode(locationBinCode);
    }

    @Override
    public Long findAvailableBinIdInRow(String zone, Integer aisle, Integer rowNum) {
        return binMapper.findAvailableBinInRow(zone, aisle, rowNum);
    }

    @Override
    public Long findAvailableBinIdInZone(String zone) {
        return binMapper.findBinIdInZone(zone);
    }

    @Override
    public void incrementBinAmount(Long binId, Integer lotCount) {
        binMapper.incrementBinAmount(binId, lotCount);
    }

    @Override
    public Long findExactBinIdByBinCode(String binCode) {
        return binMapper.findExactBinIdByBinCode(binCode);
    }

    @Override
    public List<Long> findBinIdsByBinPrefix(String binCode) {
        return binMapper.findBinIdsByBinPrefix(binCode);
    }

    @Override
    public Bin findBinByBinId(Long binId) {
        return binMapper.findBinByBinId(binId);
  
    @Override
    public String findBinCode(Long binId) {
        return binMapper.findBinCode(binId);
    }
}

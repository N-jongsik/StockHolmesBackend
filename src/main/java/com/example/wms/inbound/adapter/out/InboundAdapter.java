package com.example.wms.inbound.adapter.out;

import com.example.wms.inbound.application.domain.Inbound;
import com.example.wms.inbound.application.port.out.InboundPort;
import com.example.wms.infrastructure.mapper.InboundMapper;
import com.example.wms.product.adapter.in.dto.LotInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InboundAdapter implements InboundPort {

    private final InboundMapper inboundMapper;

    @Override
    public void save(Inbound inbound) {

        inboundMapper.insert(inbound);
    }

    @Override
    public void delete(Long inboundId) {
        inboundMapper.delete(inboundId);
    }

    @Override
    public Inbound findById(Long inboundId) {
        return inboundMapper.findById(inboundId);
    }

    @Override
    public void updateIC(Long inboundId, LocalDate checkDate, String checkNumber, String inboundStatus) {
        inboundMapper.updateIC(inboundId, checkDate, checkNumber, inboundStatus);
    }

    @Override
    public Long getOrderIdByScheduleNumber(String scheduleNumber) {
        return inboundMapper.findOrderIdByScheduleNumber(scheduleNumber);
    }

    @Override
    public void updateInboundCheck(String scheduleNumber, String checkNumber) {
        inboundMapper.updateInboundWorkerCheck(scheduleNumber, checkNumber);
    }

    @Override
    public void updateOrderProduct(Long orderId, Long productId, Boolean isDefective) {
        inboundMapper.updateOrderProduct(orderId, productId, isDefective);
    }

    @Override
    public List<LotInfoDto> getLotsByScheduleNumber(String scheduleNumber) {
        return inboundMapper.findLotsByScheduleNumber(scheduleNumber);
    }

    @Override
    public List<LotInfoDto> getLotsByCheckNumber(String checkNumber) {
        return inboundMapper.findLotsByCheckNumber(checkNumber);
    }

    @Override
    public void updatePA(Long inboundId, LocalDate putAwayDate, String putAwayNumber, String inboundStatus) {
        inboundMapper.updatePA(inboundId, putAwayDate, putAwayNumber, inboundStatus);
    }
}

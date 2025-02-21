package com.example.wms.inbound.application.port.out;

import com.example.wms.inbound.application.domain.Inbound;
import com.example.wms.product.adapter.in.dto.LotInfoDto;

import java.time.LocalDate;
import java.util.List;

public interface InboundPort {
    void save(Inbound inbound);
    void delete(Long inboundId);
    Inbound findById(Long inboundId);
    void updateIC(Long inboundId, LocalDate checkDate, String checkNumber, String inboundStatus);
    Long getOrderIdByScheduleNumber(String scheduleNumber);
    void updateInboundCheck(String scheduleNumber, String checkNumber);
    void updateOrderProduct(Long orderId, Long productId, Boolean isDefective);
    List<LotInfoDto> getLotsByScheduleNumber(String scheduleNumber);
    List<LotInfoDto> getLotsByCheckNumber(String checkNumber);
    void updatePA(Long inboundId, LocalDate putAwayDate, String putAwayNumber, String inboundStatus);
}

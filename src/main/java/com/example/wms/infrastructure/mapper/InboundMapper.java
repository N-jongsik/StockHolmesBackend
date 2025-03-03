package com.example.wms.infrastructure.mapper;

import com.example.wms.inbound.adapter.in.dto.response.InboundResDto;
import com.example.wms.inbound.application.domain.Inbound;
import com.example.wms.product.adapter.in.dto.LotInfoDto;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface InboundMapper {
    void insert(Inbound inbound);
    List<InboundResDto> findInboundProductListByOrderId(Long orderId);
    void delete(Long inboundId);
    Inbound findById(Long inboundId);
    void updateIC(Long inboundId, LocalDate checkDate, String checkNumber, String inboundStatus);
    void updatePA(Long inboundId, LocalDate putAwayDate, String putAwayNumber, String inboundStatus);
    Long findOrderIdByScheduleNumber(String scheduleNumber);
    void updateInboundWorkerCheck(String scheduleNumber, String checkNumber);
    void updateOrderProduct(Long orderId, Long productId, boolean isDefective);
    List<LotInfoDto> findLotsByScheduleNumber(String scheduleNumber);
    List<LotInfoDto> findLotsByCheckNumber(String checkNumber);

}

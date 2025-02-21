package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.response.InboundProgressDetailDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundProgressResDto;
import com.example.wms.inbound.application.port.in.GetInboundByProgressUseCase;
import com.example.wms.inbound.application.port.out.InboundRetrievalPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetInboundByProgressService implements GetInboundByProgressUseCase {

    private final InboundRetrievalPort inboundRetrievalPort;

    @Override
    public Page<InboundProgressResDto> getAllInboundProgressWithPagination(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        List<InboundProgressDetailDto> inboundList = inboundRetrievalPort.findAllInboundProgressWithPagination(startDate, endDate, pageable);

        List<InboundProgressDetailDto> scheduleList = inboundList.stream()
                .filter(i -> i.getCheckNumber() == null && i.getPutAwayNumber() == null)
                .toList();

        List<InboundProgressDetailDto> checkList = inboundList.stream()
                .filter(i -> i.getCheckNumber() != null && i.getPutAwayNumber() == null)
                .toList();

        List<InboundProgressDetailDto> putAwayList = inboundList.stream()
                .filter(i -> i.getPutAwayNumber() != null)
                .toList();

        List<InboundProgressResDto> resultList = List.of(
                new InboundProgressResDto(scheduleList, checkList, putAwayList)
        );

        return new PageImpl<>(resultList, pageable, resultList.size());
    }

}

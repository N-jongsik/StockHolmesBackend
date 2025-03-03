package com.example.wms.inbound.application.port.in;

import com.example.wms.inbound.adapter.in.dto.request.InboundCheckReqDto;

public interface CreateInboundCheckUseCase {
    void createInboundCheck(Long inboundId, InboundCheckReqDto inboundCheckReqDto);
}

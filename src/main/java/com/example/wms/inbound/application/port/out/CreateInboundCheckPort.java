package com.example.wms.inbound.application.port.out;

import com.example.wms.inbound.adapter.in.dto.request.InboundCheckReqDto;

public interface CreateInboundCheckPort {
    void createInboundCheck(Long inboundId, InboundCheckReqDto inboundCheckReqDto);
}

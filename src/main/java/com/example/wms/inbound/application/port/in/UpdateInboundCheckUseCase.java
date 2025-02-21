package com.example.wms.inbound.application.port.in;

import com.example.wms.inbound.adapter.in.dto.request.InboundCheckUpdateReqDto;

public interface UpdateInboundCheckUseCase {
    void updateInboundCheck(Long inboundId, InboundCheckUpdateReqDto updateReqDto);
}

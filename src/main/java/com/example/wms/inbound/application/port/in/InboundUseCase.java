package com.example.wms.inbound.application.port.in;

import com.example.wms.inbound.adapter.in.dto.request.*;
import com.example.wms.inbound.adapter.in.dto.response.*;
import com.example.wms.order.application.domain.Order;
import com.example.wms.order.application.domain.OrderProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface InboundUseCase {
    List<InboundProductDto> getAllInboundProductList(OrderProduct orderProduct);
    void createInboundSchedule(Order order);
}

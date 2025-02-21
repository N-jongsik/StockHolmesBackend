package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.request.InboundCheckWorkerReqDto;
import com.example.wms.inbound.application.port.out.AssignInboundNumberPort;
import com.example.wms.inbound.application.port.out.InboundPort;
import com.example.wms.order.application.port.out.OrderPort;
import com.example.wms.product.application.port.out.LotPort;
import com.example.wms.product.application.port.out.ProductPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InboundCheckWorkerCreateTest {

    @InjectMocks
    private InboundService inboundService;

    @Mock
    private InboundPort inboundPort;

    @Mock
    private AssignInboundNumberPort assignInboundNumberPort;

    @Mock
    private LotPort lotPort;


    @Mock
    private OrderPort orderPort;

    @Mock
    private ProductPort productPort;

    @Test
    @DisplayName("작업자가 입하 검사를 생성한다.")
    public void testCreateInboundCheckWorker() {

        // given
        String scheduleNumber1 = " IS202502060001";
        String scheduleNumber2 = " IS202502060002";
        Long orderId1 = 100L;
        Long orderId2 = 101L;
        Long productId1 = 1L;
        Long productId2 = 2L;
        Long productId3 = 3L;

        List<InboundCheckWorkerReqDto> checkRequests = List.of(
                new InboundCheckWorkerReqDto(productId1, false, scheduleNumber1),
                new InboundCheckWorkerReqDto(productId2, true, scheduleNumber1),
                new InboundCheckWorkerReqDto(productId3, false, scheduleNumber2)
        );

        when(inboundPort.getOrderIdByScheduleNumber(scheduleNumber1)).thenReturn(orderId1);
        when(inboundPort.getOrderIdByScheduleNumber(scheduleNumber2)).thenReturn(orderId2);

        // when
        inboundService.createInboundCheckByWorker(List.of(checkRequests.get(0), checkRequests.get(1)));
        inboundService.createInboundCheckByWorker(List.of(checkRequests.get(2)));

        // then
        verify(inboundPort, times(1)).getOrderIdByScheduleNumber(scheduleNumber1);
        verify(inboundPort, times(1)).getOrderIdByScheduleNumber(scheduleNumber2);

        verify(inboundPort, times(1)).updateOrderProduct(orderId1, productId1, false);
        verify(inboundPort, times(1)).updateOrderProduct(orderId1, productId2, true);
        verify(inboundPort, times(1)).updateOrderProduct(orderId2, productId3, false);

        verify(inboundPort, times(1)).updateInboundCheck(eq(scheduleNumber1), anyString());
        verify(inboundPort, times(1)).updateInboundCheck(eq(scheduleNumber2), anyString());


    }
}

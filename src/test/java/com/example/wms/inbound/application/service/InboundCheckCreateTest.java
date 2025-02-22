package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.request.InboundCheckReqDto;
import com.example.wms.inbound.adapter.in.dto.request.InboundCheckedProductReqDto;
import com.example.wms.inbound.application.domain.Inbound;
import com.example.wms.inbound.application.port.out.AssignInboundNumberPort;
import com.example.wms.inbound.application.port.out.InboundPort;
import com.example.wms.order.application.domain.OrderProduct;
import com.example.wms.order.application.port.out.OrderPort;
import com.example.wms.order.application.port.out.OrderProductPort;
import com.example.wms.product.application.domain.Product;
import com.example.wms.product.application.port.out.LotPort;
import com.example.wms.product.application.port.out.ProductPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InboundCheckCreateTest {

    @InjectMocks
    private CreateInboundCheckService createInboundCheckService;

    @Mock
    private InboundPort inboundPort;

    @Mock
    private AssignInboundNumberPort assignInboundNumberPort;

    @Mock
    private LotPort lotPort;

    @Mock
    private OrderProductPort orderProductPort;

    @Mock
    private OrderPort orderPort;

    @Mock
    private ProductPort productPort;

    @Test
    public void testCreateInboundCheck() {

        // given
        InboundCheckedProductReqDto checkedProductDto1 = InboundCheckedProductReqDto.builder()
                .productId(1L) // 품목 ID
                .defectiveCount(15L) // 불합격 lot 개수
                .build();

        InboundCheckedProductReqDto checkedProductDto2 = InboundCheckedProductReqDto.builder()
                .productId(2L) // 품목 ID
                .defectiveCount(20L) // 불합격 lot 개수
                .build();

        List<InboundCheckedProductReqDto> inboundCheckedProductDtoList = Arrays.asList(checkedProductDto1, checkedProductDto2); // 수정 필요

        // given
        InboundCheckReqDto inboundCheckReqDto = InboundCheckReqDto.builder()
                .checkedProductList(inboundCheckedProductDtoList) // 검사 결과
                .build();

        Inbound inbound = Inbound.builder()
                        .inboundId(1L)
                        .scheduleNumber("IS202502060001")// 입하 예정 번호
                        .checkDate(LocalDate.now())
                        .checkNumber("IC202502060001") // 입하 검사 번호
                        .build();

        Product product1 = Product.builder()
                        .productId(1L)
                        .lotUnit(1)
                        .build();

        Product product2 = Product.builder()
                        .productId(2L)
                        .lotUnit(2)
                        .build();

        when(inboundPort.findById(1L)).thenReturn(inbound);
        when(productPort.findById(1L)).thenReturn(product1);
        when(productPort.findById(2L)).thenReturn(product2);

        // given
        OrderProduct orderProduct1 = OrderProduct.builder()
                .productId(1L)
                .build();
        OrderProduct orderProduct2 = OrderProduct.builder()
                .productId(2L)
                .build();

        lenient().when(orderProductPort.findByProductId(1L)).thenReturn(orderProduct1);
        lenient().when(orderProductPort.findByProductId(2L)).thenReturn(orderProduct2);

        orderProduct1.setDefectiveCount(15L);
        orderProduct2.setDefectiveCount(20L);

        // when
        createInboundCheckService.createInboundCheck(1L, inboundCheckReqDto);

        // then
        verify(inboundPort, times(2)).updateIC(eq(1L), any(LocalDate.class), any(String.class), any(String.class));


    }
}

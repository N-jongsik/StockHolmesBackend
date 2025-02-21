package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.request.InboundCheckUpdateReqDto;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InboundCheckUpdateTest {

    @InjectMocks
    private InboundService inboundService;

    @Mock
    private OrderProductPort orderProductPort;

    @Mock
    private InboundPort inboundPort;

    @Mock
    private ProductPort productPort;

    @Mock
    private AssignInboundNumberPort assignInboundNumberPort;

    @Mock
    private LotPort lotPort;

    @Mock
    private OrderPort orderPort;

    @Test
    @DisplayName("입하 검사 데이터를 정상적으로 수정할 수 있다.")
    public void testUpdateInboundCheck_Success() {

        // given
        Long inboundId = 1L;
        LocalDate updateCheckDate = LocalDate.now();

        List<InboundCheckedProductReqDto> updatedProductList = List.of(
                new InboundCheckedProductReqDto(10L, 5L),
                new InboundCheckedProductReqDto(20L, 2L)
        );

        InboundCheckUpdateReqDto updateReqDto = new InboundCheckUpdateReqDto(updatedProductList);

        Inbound inbound = Inbound.builder()
                .inboundId(inboundId)
                .checkDate(LocalDate.parse("2025-02-10"))
                .build();



        when(inboundPort.findById(inboundId)).thenReturn(inbound);

        for (InboundCheckedProductReqDto checkedProduct : updatedProductList) {
            Product product = Product.builder()
                    .productId(checkedProduct.getProductId())
                    .supplierId(100L)
                    .build();
            when(productPort.findById(checkedProduct.getProductId())).thenReturn(product);
        }

        when(orderProductPort.findByProductId(10L)).thenReturn(new OrderProduct(10L, 1L, 5,1L,"he",true,5L));
        when(orderProductPort.findByProductId(20L)).thenReturn(new OrderProduct(20L, 1L, 2,1L,"df",false,5L));
  
        // when
        inboundService.updateInboundCheck(inboundId, updateReqDto);

        assertEquals(updateCheckDate, inbound.getCheckDate());
        verify(inboundPort, times(1)).updateIC(inboundId,updateCheckDate ,inbound.getCheckNumber(), "입하검사");

    }




}

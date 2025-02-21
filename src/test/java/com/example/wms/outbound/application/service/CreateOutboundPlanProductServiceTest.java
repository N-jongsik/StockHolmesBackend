package com.example.wms.outbound.application.service;

import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import com.example.wms.outbound.application.domain.OutboundPlanProduct;
import com.example.wms.outbound.application.port.out.CreateOutboundPlanProductPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("출고 관련 테스트 outboundPlanProduct")
class CreateOutboundPlanProductServiceTest {

    //의존성 모킹
    @Mock
    private CreateOutboundPlanProductPort createOutboundPlanProductPort;

    //테스트 대상
    @InjectMocks
    private CreateOutboundPlanProductService createOutboundPlanProductService;

    private Long outboundPlanId;
    private Integer stockQuantity;
    private Integer orderQuantity;
    private String status;

    private List<ProductInfoDto> productInfoDtoList;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        //given
        outboundPlanId = 1L;
        stockQuantity = 50;
        orderQuantity = 100;
        status = "진행중";

        productInfoDtoList = List.of(
                ProductInfoDto.builder().productId(1L).productCount(30).build(),
                ProductInfoDto.builder().productId(2L).productCount(20).build()
        );
    }

    @Test
    @DisplayName("outboundPlanProduct 잘 저장되는지 확인하는 테스트")
    void testCreateOutboundPlanProduct() {
        // when -> 저장하기
        createOutboundPlanProductService.createOutboundPlanProduct(outboundPlanId,productInfoDtoList);

        // then -> saveAll 함수호출 한번인지 확인
        verify(createOutboundPlanProductPort, times(1)).saveAll(anyList());

        // then -> ArgumentCaptor로 값 확인하기
        ArgumentCaptor<List<OutboundPlanProduct>> captor = ArgumentCaptor.forClass(List.class);
        verify(createOutboundPlanProductPort).saveAll(captor.capture());
        List<OutboundPlanProduct> outboundPlanProductList = captor.getValue();

        assertNotNull(outboundPlanProductList);
        assertEquals(2,outboundPlanProductList.size());
        assertEquals(outboundPlanId,outboundPlanProductList.get(0).getOutboundPlanId());
        assertEquals(outboundPlanId,outboundPlanProductList.get(1).getOutboundPlanId());
        assertEquals(stockQuantity,outboundPlanProductList.get(0).getStockUsedQuantity());
        assertEquals(stockQuantity,outboundPlanProductList.get(1).getStockUsedQuantity());
        assertEquals(orderQuantity,outboundPlanProductList.get(0).getOrderQuantity());
        assertEquals(orderQuantity,outboundPlanProductList.get(1).getOrderQuantity());
        assertEquals(status,outboundPlanProductList.get(0).getStatus());
        assertEquals(status,outboundPlanProductList.get(1).getStatus());
        assertEquals(1L,outboundPlanProductList.get(0).getProductId());
        assertEquals(2L,outboundPlanProductList.get(1).getProductId());
        assertEquals(30,outboundPlanProductList.get(0).getRequiredQuantity());
        assertEquals(20,outboundPlanProductList.get(1).getRequiredQuantity());
    }

    @Test
    @DisplayName("productInfoDto가 null이 들어오면 NullPointerException발생")
    void testCreateOutboundPlanProductNull() {
        //when & then
        assertThrows(NullPointerException.class, () ->
                createOutboundPlanProductService.createOutboundPlanProduct(outboundPlanId, null));
    }
}
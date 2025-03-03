package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.request.InboundCheckReqDto;
import com.example.wms.inbound.adapter.in.dto.request.InboundCheckedProductReqDto;
import com.example.wms.inbound.application.domain.Inbound;
import com.example.wms.inbound.application.port.out.AssignInboundNumberPort;
import com.example.wms.inbound.application.port.out.InboundPort;
import com.example.wms.infrastructure.exception.NotFoundException;
import com.example.wms.inventory.application.port.out.InventoryPort;
import com.example.wms.order.application.domain.OrderProduct;
import com.example.wms.order.application.port.out.OrderPort;
import com.example.wms.order.application.port.out.OrderProductPort;
import com.example.wms.product.application.domain.Lot;
import com.example.wms.product.application.domain.LotStatus;
import com.example.wms.product.application.domain.Product;
import com.example.wms.product.application.port.in.BinUseCase;
import com.example.wms.product.application.port.out.LotPort;
import com.example.wms.product.application.port.out.ProductPort;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class CreateInboundCheckServiceTest {

    private CreateInboundCheckService createInboundCheckService;

    @Mock
    private InboundPort inboundPort;

    @Mock
    private ProductPort productPort;

    @Mock
    private AssignInboundNumberPort assignInboundNumberPort;

    @Mock
    private OrderPort orderPort;

    @Mock
    private OrderProductPort orderProductPort;

    @Mock
    private BinUseCase binUseCase;

    @Mock
    private LotPort lotPort;

    @Mock
    private InventoryPort inventoryPort;

    private Long inboundId;
    private InboundCheckReqDto inboundCheckReqDto;
    private Product product;
    private Inbound inbound;
    private OrderProduct orderProduct;

    @Before
    public void setUp() {
        createInboundCheckService = new CreateInboundCheckService(
                inboundPort,
                productPort,
                assignInboundNumberPort,
                orderPort,
                orderProductPort,
                binUseCase,
                lotPort,
                inventoryPort
        );

        inboundId = 1L;

        product = Product.builder()
                .productId(1L)
                .supplierId(1L)
                .productName("Test product")
                .build();

        inbound = Inbound.builder()
                .inboundId(inboundId)
                .orderId(1L)
                .build();

        orderProduct = OrderProduct.builder()
                .orderProductId(1L)
                .orderId(1L)
                .productId(1L)
                .productCount(10)
                .productName("Test product")
                .isDefective(false)
                .defectiveCount(0L)
                .build();

        InboundCheckedProductReqDto checkedProductReqDto = new InboundCheckedProductReqDto();
        checkedProductReqDto.setProductId(1L);
        checkedProductReqDto.setDefectiveCount(2L);

        inboundCheckReqDto = new InboundCheckReqDto();
        inboundCheckReqDto.setCheckedProductList(List.of(checkedProductReqDto));
    }

    @Test
    public void createInboundCheck_Success() {

        // given
        when(inboundPort.findById(inboundId)).thenReturn(inbound);
        when(productPort.findById(1L)).thenReturn(product);
        when(orderProductPort.findByOrderId(1L, 1L)).thenReturn(orderProduct);
        when(productPort.getLocationBinCode(1L)).thenReturn("LOC-001");
        when(binUseCase.assignBinIdsToLots(anyString(), anyInt())).thenReturn(Arrays.asList(1L,2L));
        when(assignInboundNumberPort.findMaxLONumber()).thenReturn(null);

        createInboundCheckService.createInboundCheck(inboundId, inboundCheckReqDto);

        verify(orderProductPort).update(eq(1L), eq(2L));
        verify(inventoryPort).updateInventory(eq(1L), eq(8));
        verify(productPort).updateRequiredQuantity(eq(1L),eq(8));
        verify(lotPort, times(8)).insertLot(any(Lot.class));
        verify(inboundPort).updateIC(eq(inboundId), any(LocalDate.class), anyString(), eq("입하검사"));
        verify(orderPort).createOrderWithSupplier(eq(1L), eq(inboundId));
    }

    @Test(expected = NotFoundException.class)
    public void createInboundCheck_NotFoundInbound() {

        when(inboundPort.findById(inboundId)).thenReturn(null);

        createInboundCheckService.createInboundCheck(inboundId, inboundCheckReqDto);
    }

    @Test(expected = NotFoundException.class)
    public void createInboundCheck_NotFoundProduct() {

        when(inboundPort.findById(inboundId)).thenReturn(inbound);
        when(productPort.findById(1L)).thenReturn(null);

        createInboundCheckService.createInboundCheck(inboundId, inboundCheckReqDto);
    }

    @Test
    public void createInboundCheck_LotNumberGeneration() {
        // Given
        when(inboundPort.findById(inboundId)).thenReturn(inbound);
        when(productPort.findById(1L)).thenReturn(product);
        when(orderProductPort.findByOrderId(1L, 1L)).thenReturn(orderProduct);
        when(productPort.getLocationBinCode(1L)).thenReturn("LOC-001");
        when(binUseCase.assignBinIdsToLots(anyString(), anyInt())).thenReturn(Arrays.asList(1L, 2L));
        when(assignInboundNumberPort.findMaxLONumber()).thenReturn("LO202502230001");

        // When
        createInboundCheckService.createInboundCheck(inboundId, inboundCheckReqDto);

        // Then
        verify(lotPort, times(8)).insertLot(argThat(lot ->
                lot.getLotNumber().startsWith("LO") &&
                        lot.getStatus() == LotStatus.입고 &&
                        lot.getInboundId().equals(inboundId)
        ));
    }

    @Test
    public void createInboundCheck_NoDefectiveProducts() {
        // Given
        InboundCheckedProductReqDto noDefectiveProduct = new InboundCheckedProductReqDto();
        noDefectiveProduct.setProductId(1L);
        noDefectiveProduct.setDefectiveCount(0L);

        InboundCheckReqDto reqDto = new InboundCheckReqDto();
        reqDto.setCheckedProductList(List.of(noDefectiveProduct));

        when(inboundPort.findById(inboundId)).thenReturn(inbound);
        when(productPort.findById(1L)).thenReturn(product);
        when(orderProductPort.findByOrderId(1L, 1L)).thenReturn(orderProduct);
        when(productPort.getLocationBinCode(1L)).thenReturn("LOC-001");
        when(binUseCase.assignBinIdsToLots(anyString(), anyInt())).thenReturn(Arrays.asList(1L, 2L));

        // When
        createInboundCheckService.createInboundCheck(inboundId, reqDto);

        // Then
        verify(orderPort, never()).createOrderWithSupplier(anyLong(), anyLong());
        verify(orderProductPort, never()).save(any(OrderProduct.class));
    }
}
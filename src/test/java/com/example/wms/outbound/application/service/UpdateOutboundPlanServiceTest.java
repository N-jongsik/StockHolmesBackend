package com.example.wms.outbound.application.service;

import com.example.wms.outbound.adapter.in.dto.OutboundPlanRequestDto;
import com.example.wms.outbound.adapter.in.dto.ProductInfoDto;
import com.example.wms.outbound.application.port.out.UpdateOutboundPlanPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

class UpdateOutboundPlanServiceTest {

    @Mock
    private UpdateOutboundPlanPort updateOutboundPlanPort;

    @InjectMocks
    private UpdateOutboundPlanService updateOutboundPlanService;

    private OutboundPlanRequestDto outboundPlanRequestDto;
    private Long outboundPlanId;
    private List<ProductInfoDto> productInfoDtoList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        productInfoDtoList = List.of(
                ProductInfoDto.builder().productId(1L).productCount(30).build(),
                ProductInfoDto.builder().productId(2L).productCount(20).build()
        );

        // 테스트용 DTO 설정
        outboundPlanId = 1L;
        outboundPlanRequestDto = OutboundPlanRequestDto.builder()
                .outboundScheduleDate(LocalDate.of(2025, 2, 10))
                .planDate(LocalDate.of(2025, 2, 10))
                .productList(productInfoDtoList)
                .build();
    }

    @Test
    @DisplayName("출고 계획 수정 서비스 메서드 호출 및 값 확인")
    void testUpdateOutboundPlan() {
        // given: 주어진 값, 의존성 설정

        // when: 실제 서비스 메서드 호출
        updateOutboundPlanService.UpdateOutboundPlan(outboundPlanId, outboundPlanRequestDto);

        // ArgumentCaptor로 전달된 값 캡처
        ArgumentCaptor<OutboundPlanRequestDto> outboundPlanCaptor = ArgumentCaptor.forClass(OutboundPlanRequestDto.class);
        ArgumentCaptor<List<ProductInfoDto>> productListCaptor = ArgumentCaptor.forClass(List.class);

        // then: 서비스 메서드가 호출되었는지 검증
        verify(updateOutboundPlanPort, times(1)).updateOutboundPlan(outboundPlanId, outboundPlanCaptor.capture());
        verify(updateOutboundPlanPort, times(1)).updateOutboundPlanProducts(outboundPlanId, (OutboundPlanRequestDto) productListCaptor.capture());

        // 캡처한 값 검증
        OutboundPlanRequestDto capturedOutboundPlan = outboundPlanCaptor.getValue();
        List<ProductInfoDto> capturedProductList = productListCaptor.getValue();

        // outboundPlan의 값이 올바르게 수정되었는지 확인
        assert capturedOutboundPlan.getOutboundScheduleDate().equals(LocalDate.of(2025, 2, 10));
        assert capturedOutboundPlan.getPlanDate().equals(LocalDate.of(2025, 2, 10));

        // productList의 값이 올바르게 수정되었는지 확인
        assert capturedProductList.size() == 2;
        assert capturedProductList.get(0).getProductId().equals(1L);
        assert capturedProductList.get(0).getProductCount().equals(30);
        assert capturedProductList.get(1).getProductId().equals(2L);
        assert capturedProductList.get(1).getProductCount().equals(20);
    }
}

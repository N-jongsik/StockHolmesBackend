package com.example.wms.outbound.application.service;

import com.example.wms.outbound.application.port.out.DeleteOutboundPlanProductPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.Mockito.*;

class DeleteOutboundPlanProductServiceTest {

    // 의존성 모킹
    @Mock
    private DeleteOutboundPlanProductPort deleteOutboundPlanProductPort;

    // 테스트 대상
    @InjectMocks
    private DeleteOutboundPlanProductService deleteOutboundPlanProductService;

    private Long outboundPlanId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        outboundPlanId = 1L; // 테스트용 outboundPlanId 설정
    }

    @Test
    @DisplayName("정상적인 출고 에정 삭제 테스트")
    void testDeleteOutboundPlanProduct() {
        // Given: deleteOutboundPlanProductPort의 deleteOutboundPlanProduct 메서드가 호출되기를 기대
        doNothing().when(deleteOutboundPlanProductPort).deleteOutboundPlanProduct(outboundPlanId);

        // When: deleteOutboundPlanProductService의 deleteOutboundPlanProduct 호출
        deleteOutboundPlanProductService.deleteOutboundPlanProduct(outboundPlanId);

        // Then: deleteOutboundPlanProductPort의 deleteOutboundPlanProduct 메서드가 1번 호출되었는지 확인
        verify(deleteOutboundPlanProductPort, times(1)).deleteOutboundPlanProduct(outboundPlanId);
    }

    @Test
    @DisplayName("예외를 잘 발생시키는 지 테스트")
    void testDeleteOutboundPlanProduct_withException() {
        // Given: deleteOutboundPlanProductPort에서 예외가 발생한다고 가정
        doThrow(new RuntimeException("Outbound Plan Product delete failed")).when(deleteOutboundPlanProductPort).deleteOutboundPlanProduct(outboundPlanId);

        // When: deleteOutboundPlanProductService의 deleteOutboundPlanProduct 호출
        Throwable thrown = catchThrowable(() -> deleteOutboundPlanProductService.deleteOutboundPlanProduct(outboundPlanId));

        // Then: 예외가 발생해야 한다.
        assertThat(thrown).isInstanceOf(RuntimeException.class).hasMessageContaining("Outbound Plan Product delete failed");

        // Then: deleteOutboundPlanProductPort는 1번 호출되었어야 한다.
        verify(deleteOutboundPlanProductPort, times(1)).deleteOutboundPlanProduct(outboundPlanId);
    }
}

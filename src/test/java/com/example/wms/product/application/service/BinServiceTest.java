package com.example.wms.product.application.service;

import com.example.wms.product.adapter.in.dto.BinResponseDto;
import com.example.wms.product.adapter.out.dto.FlatBinDto;
import com.example.wms.product.application.port.out.BinPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BinServiceTest {

    @Mock
    private BinPort binPort;

    @InjectMocks
    private BinService binService;

    @Test
    void findAllBinsWithDetails() {

        //given
        List<FlatBinDto> mockFlatBinDtos = List.of(
                new FlatBinDto(1L, "A01", "Zone A", 3, 5, 2, 10,
                        1001L, "50001", 2001L, "입고", 3001L, null,
                        "P-5001", "상품A"),

                new FlatBinDto(1L, "A01", "Zone A", 3, 5, 2, 10,
                        1002L, "50002", 2002L, "출고중", 3002L, 4002L,
                        "P-5002", "상품B"),

                new FlatBinDto(2L, "B01", "Zone B", 4, 6, 3, 15,
                        1003L, "50003", 2003L, "입고", 3003L, null,
                        "P-5003", "상품C")
        );


        when(binPort.selectAllBinsWithDetails()).thenReturn(mockFlatBinDtos);

        //when
        List<BinResponseDto> dtoList = binService.findAllBinsWithDetails();

        //then
        assertAll(
                () -> assertThat(dtoList).isNotNull(), // dtoList가 null이 아님
                () -> assertThat(dtoList).hasSize(2),  // BIN 개수 검증

                // 첫 번째 BIN 검증
                () -> {
                    BinResponseDto firstBin = dtoList.get(0);
                    assertAll("첫 번째 BIN 검증",
                            () -> assertThat(firstBin.getBinId()).isEqualTo(1L),
                            () -> assertThat(firstBin.getBinCode()).isEqualTo("A01"),
                            () -> assertThat(firstBin.getZone()).isEqualTo("Zone A"),
                            () -> assertThat(firstBin.getAisle()).isEqualTo(3),
                            () -> assertThat(firstBin.getRow()).isEqualTo(5),
                            () -> assertThat(firstBin.getFloor()).isEqualTo(2),
                            () -> assertThat(firstBin.getAmount()).isEqualTo(10),
                            () -> assertThat(firstBin.getLotList()).hasSize(2) // LOT 2개 확인
                    );
                },

                // 첫 번째 BIN 내 LOT 검증
                () -> {
                    var firstLot = dtoList.get(0).getLotList().get(0);
                    assertAll("첫 번째 LOT 검증",
                            () -> assertThat(firstLot.getLotId()).isEqualTo(1001L),
                            () -> assertThat(firstLot.getLotNumber()).isEqualTo("50001"),
                            () -> assertThat(firstLot.getProductId()).isEqualTo(2001L),
                            () -> assertThat(firstLot.getStatus()).isEqualTo("입고"),
                            () -> assertThat(firstLot.getInboundId()).isEqualTo(3001L),
                            () -> assertThat(firstLot.getOutboundId()).isNull()
                    );
                },

                // 첫 번째 BIN 내 두 번째 LOT 검증
                () -> {
                    var secondLot = dtoList.get(0).getLotList().get(1);
                    assertAll("두 번째 LOT 검증",
                            () -> assertThat(secondLot.getLotId()).isEqualTo(1002L),
                            () -> assertThat(secondLot.getLotNumber()).isEqualTo("50002"),
                            () -> assertThat(secondLot.getProductId()).isEqualTo(2002L),
                            () -> assertThat(secondLot.getStatus()).isEqualTo("출고중"),
                            () -> assertThat(secondLot.getInboundId()).isEqualTo(3002L),
                            () -> assertThat(secondLot.getOutboundId()).isEqualTo(4002L)
                    );
                },


                // 첫 번째 LOT 내 PRODUCT 검증
                () -> {
                    var firstProduct = dtoList.get(0).getLotList().get(0).getProductInBinDto();
                    assertAll("첫 번째 PRODUCT 검증",
                            () -> assertThat(firstProduct.getProductCode()).isEqualTo("P-5001"),
                            () -> assertThat(firstProduct.getProductName()).isEqualTo("상품A")
                    );
                },

                // 두 번째 LOT 내 PRODUCT 검증
                () -> {
                    var secondProduct = dtoList.get(0).getLotList().get(1).getProductInBinDto();
                    assertAll("두 번째 PRODUCT 검증",
                            () -> assertThat(secondProduct.getProductCode()).isEqualTo("P-5002"),
                            () -> assertThat(secondProduct.getProductName()).isEqualTo("상품B")
                    );
                },

                // 두 번째 BIN 검증
                () -> {
                    BinResponseDto secondBin = dtoList.get(1);
                    assertAll("두 번째 BIN 검증",
                            () -> assertThat(secondBin.getBinId()).isEqualTo(2L),
                            () -> assertThat(secondBin.getBinCode()).isEqualTo("B01"),
                            () -> assertThat(secondBin.getZone()).isEqualTo("Zone B"),
                            () -> assertThat(secondBin.getAisle()).isEqualTo(4),
                            () -> assertThat(secondBin.getRow()).isEqualTo(6),
                            () -> assertThat(secondBin.getFloor()).isEqualTo(3),
                            () -> assertThat(secondBin.getAmount()).isEqualTo(15),
                            () -> assertThat(secondBin.getLotList()).hasSize(1) // LOT 1개 확인
                    );
                },

                // 두 번째 BIN 내 LOT 검증
                () -> {
                    var secondLot = dtoList.get(1).getLotList().get(0);
                    assertAll("두 번째 LOT 검증",
                            () -> assertThat(secondLot.getLotId()).isEqualTo(1003L),
                            () -> assertThat(secondLot.getLotNumber()).isEqualTo("50003"),
                            () -> assertThat(secondLot.getProductId()).isEqualTo(2003L),
                            () -> assertThat(secondLot.getStatus()).isEqualTo("입고"),
                            () -> assertThat(secondLot.getInboundId()).isEqualTo(3003L),
                            () -> assertThat(secondLot.getOutboundId()).isNull()
                    );
                },

                // 두 번째 LOT 내 PRODUCT 검증
                () -> {
                    var secondProduct = dtoList.get(1).getLotList().get(0).getProductInBinDto();
                    assertAll("두 번째 PRODUCT 검증",
                            () -> assertThat(secondProduct.getProductCode()).isEqualTo("P-5003"),
                            () -> assertThat(secondProduct.getProductName()).isEqualTo("상품C")
                    );
                }
        );


    }
}

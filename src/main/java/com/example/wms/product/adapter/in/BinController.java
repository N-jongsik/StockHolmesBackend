package com.example.wms.product.adapter.in;

import com.example.wms.product.adapter.in.dto.BinResponseDto;
import com.example.wms.product.application.port.in.BinUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bin")
@Tag(name = "Bin", description = "Bin 관련 API")
public class BinController {
    private final BinUseCase binUseCase;

    @GetMapping
    @Operation(
            summary = "전체 Bin 조회",
            description = "모든 Bin의 상세 정보를 조회합니다. 각 Bin 객체는 해당 Bin에 포함된 Lot 목록을 포함하며, 각 Lot은 품목(Product) 정보를 포함합니다."
    )
    public ResponseEntity<List<BinResponseDto>> findAllBinsWithDetails() {
        return ResponseEntity.ok(binUseCase.findAllBinsWithDetails());
    }
}

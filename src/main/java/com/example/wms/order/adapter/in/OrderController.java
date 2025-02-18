package com.example.wms.order.adapter.in;

import com.example.wms.notification.application.domain.Notification;
import com.example.wms.notification.application.port.in.NotificationUseCase;
import com.example.wms.order.adapter.in.dto.OrderRequestDto;
import com.example.wms.order.adapter.in.dto.OrderResponseDto;
import com.example.wms.order.adapter.in.dto.ProductListDto;
import com.example.wms.order.application.domain.Order;
import com.example.wms.order.application.port.in.*;
import com.example.wms.user.application.domain.enums.UserRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Tag(name = "발주 관련 API")
public class OrderController {

    private final RegisterOrderUseCase registerOrderUseCase;
    private final RegisterOrderProductUseCase registerOrderProductUseCase;
    private final DeleteOrderUseCase deleteOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final UpdateOrderUseCase updateOrderUseCase;
    private final NotificationUseCase notificationUseCase;

    @PostMapping
    @Operation(summary = "발주 등록하기")
    public ResponseEntity<Void> order(@RequestBody OrderRequestDto orderRequestDto) {
        Long orderId = registerOrderUseCase.registerOrder(orderRequestDto);
        Notification notification = registerOrderProductUseCase.registerOrderProduct(orderId, orderRequestDto.getProductList());
        notificationUseCase.send(UserRole.ROLE_ADMIN, notification);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{orderId}")
    @Operation(summary = "발주 삭제하기", description = "발주 승인 전 가능, 이미 승인된 발주 500에러")
    public ResponseEntity<Void> cancel(@PathVariable("orderId") Long orderId) {
        Order order = getOrderUseCase.getOrder(orderId);

        if (order.getIsApproved()) {
            throw new IllegalStateException("이미 승인된 발주는 삭제할 수 없습니다.");
        }

        deleteOrderUseCase.deleteOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{orderId}")
    @Operation(summary = "발주 수정하기", description = "발주 승인 전 가능, 이미 승인된 발주 500에러")
    public ResponseEntity<Void> update(@PathVariable("orderId") Long orderId, @RequestBody List<ProductListDto> productList) {
        Order order = getOrderUseCase.getOrder(orderId);

        if (order.getIsApproved()) {
            throw new IllegalStateException("이미 승인된 발주는 수정할 수 없습니다.");
        }

        updateOrderUseCase.updateOrder(orderId, productList);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "발주 조회하기")
    public ResponseEntity<?> getProductList(
            @RequestParam(value = "number", required = false) String orderNumber,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(getOrderUseCase.getFilteredOrder(orderNumber, startDate, endDate, pageable));
    }
}

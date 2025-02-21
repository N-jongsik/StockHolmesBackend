package com.example.wms.worker.adapter.in;

import com.example.wms.worker.adapter.in.dto.response.WorkerInboundResDto;
import com.example.wms.worker.application.port.in.WorkerInboundUseCase;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/worker")
@RequiredArgsConstructor
public class WorkerInboundController {

    private final WorkerInboundUseCase workerInboundUseCase;

    @GetMapping("/inbound")
    @Operation(summary = "작업자용 입하 예정 리스트 조회" , description = "입하예정번호와 시작일, 종료일을 입력해 입하 예정 데이터를 검색 조건에 따라 조회합니다.")
    public ResponseEntity<List<WorkerInboundResDto>> getInboundPlans(@RequestParam(value = "todayDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate todayDate)
    {
        return ResponseEntity.ok(workerInboundUseCase.getFilteredWorkerInboundList(todayDate));
    }

}

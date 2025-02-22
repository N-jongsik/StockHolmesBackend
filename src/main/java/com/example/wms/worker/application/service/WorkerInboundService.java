package com.example.wms.worker.application.service;

import com.example.wms.inbound.application.port.out.AssignInboundNumberPort;
import com.example.wms.inbound.application.port.out.InboundPort;
import com.example.wms.infrastructure.pagination.util.PageableUtils;
import com.example.wms.product.application.domain.Lot;
import com.example.wms.product.application.domain.LotStatus;
import com.example.wms.product.application.port.out.BinPort;
import com.example.wms.product.application.port.out.LotPort;
import com.example.wms.product.application.port.out.ProductPort;
import com.example.wms.worker.adapter.in.dto.request.WorkerInboundCheckProductReqDto;
import com.example.wms.worker.adapter.in.dto.request.WorkerInboundCheckReqDto;
import com.example.wms.worker.adapter.in.dto.response.WorkerInboundCheckLotResDto;
import com.example.wms.worker.adapter.in.dto.response.WorkerInboundCheckResDto;
import com.example.wms.worker.adapter.in.dto.response.WorkerInboundResDto;
import com.example.wms.worker.application.port.in.WorkerInboundUseCase;
import com.example.wms.worker.application.port.out.WorkerInboundPort;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class WorkerInboundService implements WorkerInboundUseCase {

    private final WorkerInboundPort workerInboundPort;
    private final AssignInboundNumberPort assignInboundNumberPort;
    private final InboundPort inboundPort;
    private final BinPort binPort;
    private final LotPort lotPort;
    private final ProductPort productPort;
    private final TransactionTemplate transactionTemplate;
    private static final AtomicLong counter = new AtomicLong(100L);

    public static Long getNextId() {
        return counter.incrementAndGet();
    }
    @Override
    @Transactional(readOnly = true)
    public List<WorkerInboundResDto> getFilteredWorkerInboundList(LocalDate todayDate) {
        List<WorkerInboundResDto> workerInboundList = workerInboundPort.findFilteredWorkerInboundList(todayDate);
        return workerInboundList;
    }

    @Override
    @Transactional
    public WorkerInboundCheckResDto createWorkerInboundCheck(Long inboundId, WorkerInboundCheckReqDto dto) {

        String checkNumber = makeNumber();
        int size = dto.getProductList().size();
        Long[] arr = new Long[size];
        List<WorkerInboundCheckLotResDto> lotDtos = new ArrayList<>();

        if (inboundId != null) {
            inboundPort.updateIC(inboundId, LocalDate.now(), checkNumber, "입하검사");
        }

        transactionTemplate.execute(status -> {
            for (int i = 0; i < dto.getProductList().size(); i++) {
                Long productId = dto.getProductList().get(i).getProductId();
                Lot lot = Lot.builder()
                        .productId(productId)
                        .binId(getNextId())
                        .lotNumber(makeNumber())
                        .status(LotStatus.입고)
                        .inboundId(inboundId)
                        .build();
                lotPort.insertLot(lot);

                arr[i] = lot.getLotId();

                WorkerInboundCheckLotResDto lotResDto = WorkerInboundCheckLotResDto.builder()
                        .lotId(arr[i])
                        .lotCode(makeNumber())
                        .productId(lotPort.findById(arr[i]).getProductId())
                        .productName(productPort.findById(lotPort.findById(arr[i]).getProductId()).getProductName())
                        .productCode(productPort.findById(lotPort.findById(arr[i]).getProductId()).getProductCode())
                        .binCode(binPort.findBinCode(lotPort.findById(arr[i]).getBinId()))
                        .build();
                lotDtos.add(lotResDto);
            }
            return null;
        });

        for (WorkerInboundCheckProductReqDto product : dto.getProductList()) {
            workerInboundPort.updateLotDefectiveStatus(product.getProductId(), product.getIsDefective());
        }

        return WorkerInboundCheckResDto.builder()
                .inboundId(inboundId)
                .checkNumber(checkNumber)
                .lotList(lotDtos)
                .build();
    }

    private String makeNumber() {
        String currentDate = LocalDate.now().toString().replace("-","");

        String number = assignInboundNumberPort.findMaxICNumber();

        String nextNumber = "0000";

        if (number != null) {
            int lastNumber = Integer.parseInt(number.substring(number.length()-4));
            nextNumber = String.format("%04d", lastNumber+1);
        }

        return "IC" + currentDate + nextNumber;
    }
}


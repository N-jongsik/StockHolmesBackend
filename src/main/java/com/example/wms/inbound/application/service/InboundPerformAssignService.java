package com.example.wms.inbound.application.service;

import com.example.wms.product.application.port.in.ProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InboundPerformAssignService {

    private final ProductUseCase productUseCase;

    @Scheduled(cron = "0 0 * * * ?") // 1시간 마다 실행
    public void schedule(){
        productUseCase.performABCAnalysis();
        productUseCase.assignLocationBinCode();
    }

}

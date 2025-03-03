package com.example.wms.pdf.application.port.in;

import java.io.IOException;

public interface PdfUseCase {
    byte[] generateOutboundReport(Long outboundPlanId) throws IOException;
}

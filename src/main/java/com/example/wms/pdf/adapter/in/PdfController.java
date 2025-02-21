package com.example.wms.pdf.adapter.in;

import com.example.wms.pdf.application.port.in.PdfUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pdf")
@RequiredArgsConstructor
@Tag(name = "PDF 출력 테스트")
public class PdfController {

    private final PdfUseCase pdfUseCase;

    @Operation(summary = "출고 정보 PDF 문서 다운로드")
    @GetMapping("/generate")
    public ResponseEntity<byte[]> generatePdf() {
        try {
            byte[] pdfBytes = pdfUseCase.generateOutboundReport();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "출고지시서.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

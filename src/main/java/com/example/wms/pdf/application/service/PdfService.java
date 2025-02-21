package com.example.wms.pdf.application.service;

import com.example.wms.outbound.application.port.out.GetOutboundAssignPort;
import com.example.wms.pdf.application.port.in.PdfUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfService implements PdfUseCase{

    private final GetOutboundAssignPort getOutboundAssignPort;

    @Override
    public byte[] generateOutboundReport() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // 폰트 로드 (리소스 폴더에서 가져오기)
        String fontPath = new ClassPathResource("fonts/NanumGothic.ttf").getFile().getAbsolutePath();
        PdfFont font = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H);

        // 제목 추가
        document.add(new Paragraph("📦 출고 내역서").setFont(font).setBold().setFontSize(18));
        document.add(new Paragraph("\n"));

        // 출고 데이터 (더미 데이터)
        List<String[]> outboundData = List.of(
                new String[]{"001", "2024-02-13", "상품 A", "10", "김철수"},
                new String[]{"002", "2024-02-12", "상품 B", "5", "이영희"},
                new String[]{"003", "2024-02-11", "상품 C", "20", "박민수"}
        );

        // 테이블 생성
        float[] columnWidths = {50f, 100f, 150f, 50f, 100f};
        Table table = new Table(columnWidths);

        // 테이블 헤더 추가
        table.addHeaderCell(new Cell().add(new Paragraph("번호").setFont(font)));
        table.addHeaderCell(new Cell().add(new Paragraph("출고일").setFont(font)));
        table.addHeaderCell(new Cell().add(new Paragraph("상품명").setFont(font)));
        table.addHeaderCell(new Cell().add(new Paragraph("수량").setFont(font)));
        table.addHeaderCell(new Cell().add(new Paragraph("담당자").setFont(font)));

        // 테이블 데이터 추가
        for (String[] row : outboundData) {
            for (String cellData : row) {
                table.addCell(new Cell().add(new Paragraph(cellData).setFont(font)));
            }
        }

        document.add(table);
        document.close();

        return outputStream.toByteArray();
    }
}

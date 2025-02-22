package com.example.wms.pdf.application.service;

import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.domain.OutboundPlanProduct;
import com.example.wms.outbound.application.port.out.GetOutboundAssignPort;
import com.example.wms.pdf.application.port.in.PdfUseCase;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfService implements PdfUseCase {

    private final GetOutboundAssignPort getOutboundAssignPort;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");

    @Override
    public byte[] generateOutboundReport(Long outboundPlanId) throws IOException {
        OutboundPlan outboundPlan = getOutboundAssignPort.findOutboundPlanByOutboundPlanId(outboundPlanId);
        //List<OutboundPlanProduct> products = getOutboundAssignPort.getOutboundPlanProducts(outboundPlanId);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // 폰트 설정
        String fontPath = new ClassPathResource("fonts/NanumGothic.ttf").getFile().getAbsolutePath();
        PdfFont font = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H);
        document.setFont(font);

        // 문서 헤더 추가
        addHeader(document, outboundPlan);

        // 기본 정보 테이블 추가
        addBasicInfoTable(document, outboundPlan);

        // 제품 목록 테이블 추가
        //addProductTable(document, products);

        // 하단 서명란 추가
        addSignatureSection(document);

        document.close();
        return outputStream.toByteArray();
    }

    private void addHeader(Document document, OutboundPlan outboundPlan) {
        Paragraph header = new Paragraph("출고 예정 리스트")
                .setBold()
                .setFontSize(20)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(header);

        Paragraph subHeader = new Paragraph(String.format("출고 예정 번호: %s", outboundPlan.getOutboundScheduleNumber()))
                .setFontSize(12)
                .setTextAlignment(TextAlignment.RIGHT);
        document.add(subHeader);

        document.add(new Paragraph("\n"));
    }

    private void addBasicInfoTable(Document document, OutboundPlan outboundPlan) {
        Table infoTable = new Table(new float[]{1, 1, 1, 1})
                .setWidth(UnitValue.createPercentValue(100));

        // 스타일 설정
        infoTable.setBorder(new SolidBorder(ColorConstants.BLACK, 1));

        // 기본 정보 추가
        addInfoRow(infoTable, "출고 예정일", outboundPlan.getPlanDate().format(DATE_FORMATTER),
                "생산계획번호", outboundPlan.getProductionPlanNumber());
        addInfoRow(infoTable, "상태", outboundPlan.getStatus(),
                "등록일", outboundPlan.getOutboundScheduleDate().format(DATE_FORMATTER));

        document.add(infoTable);
        document.add(new Paragraph("\n"));
    }

    private void addInfoRow(Table table, String label1, String value1, String label2, String value2) {
        table.addCell(new Cell().add(new Paragraph(label1)).setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addCell(new Cell().add(new Paragraph(value1)));
        table.addCell(new Cell().add(new Paragraph(label2)).setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addCell(new Cell().add(new Paragraph(value2)));
    }

    private void addProductTable(Document document, List<OutboundPlanProduct> products) {
        Table productTable = new Table(new float[]{1, 3, 2, 2, 2})
                .setWidth(UnitValue.createPercentValue(100));

        // 헤더 추가
        String[] headers = {"번호", "제품ID", "필요수량", "사용수량", "주문수량"};
        for (String header : headers) {
            productTable.addHeaderCell(
                    new Cell()
                            .add(new Paragraph(header))
                            .setBold()
                            .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                            .setTextAlignment(TextAlignment.CENTER)
            );
        }

        // 제품 데이터 추가
        for (int i = 0; i < products.size(); i++) {
            OutboundPlanProduct product = products.get(i);
            productTable.addCell(new Cell().add(new Paragraph(String.valueOf(i + 1))).setTextAlignment(TextAlignment.CENTER));
            productTable.addCell(new Cell().add(new Paragraph(String.valueOf(product.getProductId()))));
            productTable.addCell(new Cell().add(new Paragraph(String.valueOf(product.getRequiredQuantity()))).setTextAlignment(TextAlignment.RIGHT));
            productTable.addCell(new Cell().add(new Paragraph(String.valueOf(product.getStockUsedQuantity()))).setTextAlignment(TextAlignment.RIGHT));
            productTable.addCell(new Cell().add(new Paragraph(String.valueOf(product.getOrderQuantity()))).setTextAlignment(TextAlignment.RIGHT));
        }

        document.add(productTable);
    }

    private void addSignatureSection(Document document) {
        document.add(new Paragraph("\n\n"));

        Table signatureTable = new Table(new float[]{1, 1, 1})
                .setWidth(UnitValue.createPercentValue(100));

        // 서명란 추가
        signatureTable.addCell(new Cell().add(new Paragraph("작성자: ________________")).setBorder(null));
        signatureTable.addCell(new Cell().add(new Paragraph("검토자: ________________")).setBorder(null));
        signatureTable.addCell(new Cell().add(new Paragraph("승인자: ________________")).setBorder(null));

        document.add(signatureTable);

        // 날짜 추가
        document.add(new Paragraph("\n\n작성일자: " + LocalDate.now().format(DATE_FORMATTER))
                .setTextAlignment(TextAlignment.RIGHT));
    }
}
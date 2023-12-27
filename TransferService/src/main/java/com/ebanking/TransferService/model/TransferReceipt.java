package com.ebanking.TransferService.model;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Data
@NoArgsConstructor
@Builder

@Service
public class TransferReceipt {
    private String orderingFullName;
    private List<String> beneficiariesFullName;
    private double amount;
    private String orderingRib;
    private List<String> beneficiariesRibs;
    private String initiatedAt;

    public TransferReceipt(String orderingFullName, List<String> beneficiariesFullName, double amount,
                           String orderingRib, List<String> beneficiariesRibs, String initiatedAt) {
        this.orderingFullName = orderingFullName;
        this.beneficiariesFullName = beneficiariesFullName;
        this.amount = amount;
        this.orderingRib = orderingRib;
        this.beneficiariesRibs = beneficiariesRibs;
        this.initiatedAt = initiatedAt;
    }

    public void generateTransferReceipt(String filePath) {
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            addTransferInfo(document);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addTransferInfo(Document document)
            throws DocumentException, IOException {
        Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
        Font subTitleFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

        addTitle(document, titleFont);

        addEmptyLine(document);

        addSubtitle(document, "Ordering Full Name: " + orderingFullName + " (RIB: " + orderingRib + ")","", subTitleFont);

        // Display each beneficiary with their full name and RIB
        for (int i = 0; i < beneficiariesFullName.size(); i++) {
            String beneficiaryFullName = beneficiariesFullName.get(i);
            String beneficiaryRib = beneficiariesRibs.get(i);

            addSubtitle(document, "Beneficiary " + (i + 1) + " Full Name: " + beneficiaryFullName + " (RIB: " + beneficiaryRib + ")","", subTitleFont);
        }

        addSubtitle(document, "Amount: ", String.valueOf(amount), subTitleFont);

        addEmptyLine(document);

        addSubtitle(document, "Initiated At: ", initiatedAt, normalFont);
        addSubtitle(document, "Bank Name: ", "BANK OF AFRICA", normalFont);
        addSubtitle(document, "Transfer Type: ", "Via WALLET", normalFont);
    }

    private void addTitle(Document document, Font font) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Paragraph("Transfer Receipt", font));
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);
    }

    private void addSubtitle(Document document, String label, String value, Font font)
            throws DocumentException {
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Phrase(label, font));
        paragraph.add(new Phrase(value, font));
        document.add(paragraph);
    }

    private void addEmptyLine(Document document) throws DocumentException {
        for (int i = 0; i < 1; i++) {
            document.add(new Paragraph(" "));
        }
    }
}

package com.ebanking.TransferService.model;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
@Data
@NoArgsConstructor
@Builder
@Service
public class TransferReceiptBankToGAB {
    private String orderingFullName;
    private String beneficiaryFullName;
    private double amount;


    private String initiatedAt;
    private String transferType;
    private String reference;
    private Boolean isInitiatedFromMobile ;

    public TransferReceiptBankToGAB(String orderingFullName, String beneficiariesFullName, double amount,
                                    String initiatedAt, String transferType, String reference  , Boolean isInitiatedFromMobile) {
        this.orderingFullName = orderingFullName;
        this.beneficiaryFullName = beneficiariesFullName;
        this.amount = amount;


        this.initiatedAt = initiatedAt;
        this.transferType = transferType;
        this.reference = reference;
        this.isInitiatedFromMobile=isInitiatedFromMobile;

    }

    public void generateTransferReceipt(String filePath) {
        Document document = new Document();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Add images to the document
            addImages(document, writer,this.isInitiatedFromMobile);

            // Add transfer information
            addTransferInfo(document);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addTransferInfo(Document document) throws DocumentException, IOException {

        Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 25, Font.BOLD);
        Font subTitleFont = new Font(Font.FontFamily.TIMES_ROMAN, 17, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);


        addEmptyLines(document, 4);
        addTitle(document, titleFont);
        addEmptyLines(document, 3);

        addSubtitle(document, "-Informations de l'ordonnateur", "", subTitleFont);
        addEmptyLine(document);
        addSubtitle(document, "   Nom complet de l'ordonnateur: " + " " + orderingFullName, "", normalFont);
        addEmptyLine(document);
        addSubtitle(document, "-Informations sur le bénéficiaire", "", subTitleFont);
        addEmptyLine(document);

        // Display each beneficiary with their full n


        addSubtitle(document, "   Nom complet du bénéficiaire:", "   " + beneficiaryFullName , normalFont);


        addEmptyLine(document);

        addEmptyLine(document);

        addSubtitle(document, "Initié le                :  ", initiatedAt, normalFont);
        addSubtitle(document, "Mode de transfert        :  ", transferType, normalFont);
        addSubtitle(document, "Référence                :  ", reference, normalFont);
        addEmptyLines(document, 4);
        addSubtitle(document, "                                                                        " + "Montant total: ", String.valueOf(amount)+"0 DH", subTitleFont);
    }

    private void addTitle(Document document, Font font) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Paragraph("                         Reçu de transfert", font));
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);
    }

    private void addSubtitle(Document document, String label, String value, Font font) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Phrase(label, font));
        paragraph.add(new Phrase(value, font));
        document.add(paragraph);
    }

    private void addEmptyLines(Document document, int offset) throws DocumentException {
        for (int i = 1; i <= offset; i++) {
            addEmptyLine(document);
        }
    }

    private void addEmptyLine(Document document) throws DocumentException {
        document.add(new Paragraph(" "));
    }

    private void addImages(Document document, PdfWriter writer, Boolean isMobile) {
        try {
            // Add banqueName image to the top
            InputStream banqueNameImageStream = getClass().getClassLoader().getResourceAsStream("img/banqueName.png");
            assert banqueNameImageStream != null;
            Image banqueNameImage = Image.getInstance(IOUtils.toByteArray(banqueNameImageStream));
            banqueNameImageStream.close();

            // Add footer image to the bottom
            InputStream footerImageStream = getClass().getClassLoader().getResourceAsStream("img/footer.png");
            assert footerImageStream != null;
            Image footerImage = Image.getInstance(IOUtils.toByteArray(footerImageStream));
            footerImageStream.close();

            // Adjust the scaling and position of the banqueName image
            float banqueNameScaleWidth = PageSize.A4.getWidth();
            float banqueNameScaleHeight = banqueNameImage.getScaledHeight() * (banqueNameScaleWidth / banqueNameImage.getScaledWidth());
            banqueNameImage.scaleToFit(banqueNameScaleWidth, banqueNameScaleHeight);
            banqueNameImage.setAbsolutePosition(0, PageSize.A4.getHeight() - banqueNameScaleHeight ); // Adjust position as needed

            // Adjust the scaling and position of the footer image
            footerImage.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight()); // Adjust height as needed
            footerImage.setAbsolutePosition(0, 0);

            // Add images to the document
            PdfContentByte canvas = writer.getDirectContentUnder();
            PdfGState gState = new PdfGState();
            gState.setFillOpacity(1f); // Adjust opacity as needed

            // Add banqueName image
            canvas.saveState();
            canvas.setGState(gState);
            canvas.addImage(banqueNameImage);
            canvas.restoreState();

            // Add footer image, if not on mobile
            if (!isMobile) {
                canvas.saveState();
                canvas.setGState(gState);
                canvas.addImage(footerImage);
                canvas.restoreState();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package com.ebanking.TransferService.model;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.apache.commons.io.IOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private String transferType;
    private String reference;
    private Boolean isInitiatedFromMobile ;

    public TransferReceipt(String orderingFullName, List<String> beneficiariesFullName, double amount,
                           String orderingRib, List<String> beneficiariesRibs, String initiatedAt, String transferType, String reference,Boolean isInitiatedFromMobile) {
        this.orderingFullName = orderingFullName;
        this.beneficiariesFullName = beneficiariesFullName;
        this.amount = amount;
        this.orderingRib = orderingRib;
        this.beneficiariesRibs = beneficiariesRibs;
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
        addSubtitle(document, "   Nom complet de l'ordonnateur: " + " " + orderingFullName + "             RIB: " + orderingRib, "", normalFont);
        addEmptyLine(document);
        addSubtitle(document, "-Informations sur les bénéficiaires ", "", subTitleFont);
        addEmptyLine(document);

        // Display each beneficiary with their full name and RIB
        for (int i = 0; i < beneficiariesFullName.size(); i++) {
            String beneficiaryFullName = beneficiariesFullName.get(i);
            String beneficiaryRib = beneficiariesRibs.get(i);

            addSubtitle(document, "   Nom complet du bénéficiaire" + (i + 1) + ":", "   " + beneficiaryFullName + "             RIB: " + beneficiaryRib, normalFont);
        }

        addEmptyLine(document);

        addEmptyLine(document);

        addSubtitle(document, "Initié le                :  ", initiatedAt, normalFont);
        addSubtitle(document, "Mode de transfert        :  ", transferType, normalFont);
        addSubtitle(document, "Référence                :  ", reference, normalFont);
        addEmptyLines(document, 4);
        addSubtitle(document, "                                                                                " + "Montant total: ", String.valueOf(amount), subTitleFont);
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

    private void addImages(Document document, PdfWriter writer,Boolean isMobile) {
        try {
            // Add banqueName image to the top left
            InputStream banqueNameImageStream = getClass().getClassLoader().getResourceAsStream("img/banqueName.png");
            assert banqueNameImageStream != null;
            Image banqueNameImage = Image.getInstance(IOUtils.toByteArray(banqueNameImageStream));
            banqueNameImageStream.close();
            banqueNameImage.scaleToFit(40, 40);

            // Add footer image to the bottom
            InputStream footerImageStream = getClass().getClassLoader().getResourceAsStream("img/footer.png");
            assert footerImageStream != null;
            Image footerImage = Image.getInstance(IOUtils.toByteArray(footerImageStream));
            footerImageStream.close();

            // Set the scaling to fit the full width of the page and maintain the aspect ratio
            footerImage.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());

            // Set the absolute position at the bottom-left corner
            footerImage.setAbsolutePosition(0, 0);

            // Set the images as watermark
            PdfContentByte canvas = writer.getDirectContentUnder();
            PdfGState gState = new PdfGState();
            gState.setFillOpacity(0.5f); // Adjust opacity as needed

            // Add banqueName image as watermark
            canvas.saveState();
            canvas.setGState(gState);
            canvas.addImage(banqueNameImage, banqueNameImage.getWidth()-100, 0, 0, banqueNameImage.getHeight(), 0, PageSize.A4.getHeight() - 100);
            canvas.restoreState();
           if(!isMobile){  // Add footer image as watermark
               canvas.saveState();
               canvas.setGState(gState);
               canvas.addImage(footerImage, PageSize.A4.getWidth(), 0, 0, 150, 0, 0);
               canvas.restoreState();
           }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}

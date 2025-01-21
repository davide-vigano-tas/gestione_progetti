package eu.tasgroup.gestione.businesscomponent.utility;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;

import javax.naming.NamingException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.facade.ClienteFacade;
import eu.tasgroup.gestione.businesscomponent.model.Payment;
import eu.tasgroup.gestione.businesscomponent.model.Project;
import eu.tasgroup.gestione.businesscomponent.model.User;

public class InvoicePDFGenerator {

    public static void generatePDF(OutputStream out, Payment payment) throws IOException, DAOException, NamingException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Recupero il progetto e i dettagli del cliente
            Project project = ClienteFacade.getInstance().getProjectById(payment.getIdProgetto());
            User cliente = ClienteFacade.getInstance().getById(project.getIdCliente());

            // Costanti per il calcolo IVA
            final double IVA_PERCENT = 22.0; // 22%
            double importoBase = payment.getCifra() / (1 + (IVA_PERCENT / 100));
            double importoIVA = payment.getCifra() - importoBase;

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Aggiunge le varie sezioni del PDF
                addHeader(contentStream, payment);
                addClientDetails(contentStream, cliente);
                addPaymentDetails(contentStream, payment, importoBase, importoIVA, IVA_PERCENT);
                addFooter(contentStream);
            }

            // Scrivi il documento nel flusso di output
            document.save(out);
        }
    }

    private static void addHeader(PDPageContentStream contentStream, Payment payment) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
        contentStream.beginText();
        contentStream.setLeading(14f);
        contentStream.newLineAtOffset(50, 750);
        contentStream.showText("Azienda Gestione Progetti S.r.l.");
        contentStream.newLine();
        contentStream.showText("Via Esempio, 123 - 20100 Milano (MI)");
        contentStream.newLine();
        contentStream.showText("P.IVA: 12345678901");
        contentStream.newLine();
        contentStream.showText("Email: info@azienda.com | Telefono: +39 012 345 678");
        contentStream.newLine();
        contentStream.newLine();
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.showText("Fattura n. " + payment.getId());
        contentStream.newLine();
        contentStream.showText("Data: " + LocalDateTime.now());
        contentStream.endText();
    }

    private static void addClientDetails(PDPageContentStream contentStream, User cliente) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 640);
        contentStream.showText("Dettagli Cliente:");
        contentStream.newLine();
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.showText("Nome: " + cliente.getNome() + " " + cliente.getCognome());
        contentStream.newLine();
        contentStream.showText("Email: " + cliente.getEmail());
        contentStream.newLine();
        contentStream.endText();
    }

    private static void addPaymentDetails(PDPageContentStream contentStream, Payment payment, double importoBase, double importoIVA, double IVA_PERCENT) throws IOException {
        float startX = 50;
        float startY = 550;
        float rowHeight = 20;

        // Intestazioni tabella
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(startX, startY);
        contentStream.showText("Dettagli Pagamento:");
        contentStream.endText();

        float currentY = startY - rowHeight;
        String[][] rows = {
            { "Importo Base (escluso IVA)", String.format("%.2f", importoBase) },
            { "IVA (" + IVA_PERCENT + "%)", String.format("%.2f", importoIVA) },
            { "Totale", String.format("%.2f", payment.getCifra()) }
        };

        // Disegna righe tabella
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        for (String[] row : rows) {
            contentStream.beginText();
            contentStream.newLineAtOffset(startX, currentY);
            contentStream.showText(row[0]);
            contentStream.newLineAtOffset(300, 0);
            contentStream.showText(row[1]);
            contentStream.endText();
            currentY -= rowHeight;
        }
    }

    private static void addFooter(PDPageContentStream contentStream) throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 370);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.showText("Note:");
        contentStream.newLine();
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.showText("Grazie per aver scelto la nostra azienda. Rimaniamo a disposizione per eventuali domande.");
        contentStream.endText();
    }
}

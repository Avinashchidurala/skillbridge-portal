package com.skillbridge.skillbridge_portal.service;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class CertificateService {

    public String generateCertificate(String name, String courseName) throws IOException, DocumentException {
        String fileName = "certificates/" + name + "-" + courseName + ".pdf";

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));

        document.open();
        document.add(new Paragraph("Certificate of Completion"));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("This certifies that"));
        document.add(new Paragraph(name));
        document.add(new Paragraph("has successfully completed the course"));
        document.add(new Paragraph(courseName));
        document.add(new Paragraph("Date: " + java.time.LocalDate.now()));
        document.close();

        return fileName;
    }
}


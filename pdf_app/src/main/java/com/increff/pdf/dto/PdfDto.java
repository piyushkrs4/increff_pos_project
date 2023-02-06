package com.increff.pdf.dto;

import com.increff.pdf.model.InvoiceData;
import com.increff.pdf.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PdfDto {
    @Autowired
    private PdfService pdfService;

    public String generatePdf(InvoiceData invoiceData) {
        return pdfService.generatePdf(invoiceData);
    }

}

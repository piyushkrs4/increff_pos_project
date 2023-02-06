package com.increff.pdf.service;

import com.increff.pdf.model.InvoiceData;
import com.increff.pdf.util.PdfGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PdfService {
    @Autowired
    PdfGenerator pdfGenerator;

    public String generatePdf(InvoiceData invoiceData) {
        return pdfGenerator.xmlToPdfConverter(invoiceData);
    }

}

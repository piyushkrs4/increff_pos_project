package com.increff.pdf.service;

import com.increff.pdf.model.InvoiceData;
import com.increff.pdf.util.JavaToXml;
import com.increff.pdf.util.PdfGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PdfService {
    @Autowired
    private PdfGenerator pdfGenerator;
    @Autowired
    private JavaToXml javaToXml;

    public String generatePdf(InvoiceData invoiceData) {
        javaToXml.javaToXmlConverter(invoiceData);
        return pdfGenerator.xmlToPdfConverter();
    }

}

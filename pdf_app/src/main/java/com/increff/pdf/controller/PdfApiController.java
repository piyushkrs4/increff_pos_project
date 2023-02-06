package com.increff.pdf.controller;

import com.increff.pdf.dto.PdfDto;
import com.increff.pdf.model.InvoiceData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
@RequestMapping(path = "/api/generate-pdf")
public class PdfApiController {
    @Autowired
    private PdfDto pdfDto;

    @ApiOperation(value = "Generate Pdf")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public String generateInvoice(@RequestBody InvoiceData invoiceData) {
        return pdfDto.generatePdf(invoiceData);
    }
}

package com.increff.pos.dto;

import com.increff.pos.model.InvoiceData;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.PdfGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

import static com.increff.pos.util.ValidatorUtil.validate;

@Component
public class PdfGeneratorDto {

    private static final String PDF_FILE_PATH = "src/main/resources/com.increff.pdf/PdfFiles/invoice_";
    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    public void generatePdf(Integer orderId, String currentDateTimeData) throws ApiException {
        validate(currentDateTimeData);
        pdfGeneratorService.generatePdf(orderId, currentDateTimeData);
    }

    public InvoiceData showPlacedOrder(Integer orderId) throws ApiException {
        return pdfGeneratorService.showPlacedOrder(orderId);
    }

    public String downloadInvoice(Integer orderId) throws IOException {
        String filePath = PDF_FILE_PATH + orderId + ".pdf";

        File file = new File(filePath);
        byte[] bytesArray = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(bytesArray);
        fis.close();
        return Base64.getEncoder().encodeToString(bytesArray);
    }

}

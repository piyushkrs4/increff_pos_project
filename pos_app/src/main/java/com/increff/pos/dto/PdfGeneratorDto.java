package com.increff.pos.dto;

import com.increff.pos.model.InvoiceData;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.PdfGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static com.increff.pos.util.ValidatorUtil.validate;

@Component
public class PdfGeneratorDto {
    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    public void generatePdf(Integer orderId, String currentDateTimeData) throws ApiException {
        validate(currentDateTimeData);
        pdfGeneratorService.generatePdf(orderId, currentDateTimeData);
    }

    public InvoiceData showPlacedOrder(Integer orderId) throws ApiException {

        return pdfGeneratorService.showPlacedOrder(orderId);
    }

}

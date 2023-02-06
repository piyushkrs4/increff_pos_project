package com.increff.pdf.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InvoiceData {

    private Integer invoiceNumber;
    private String invoiceDate;
    private String invoiceTime;
    private Double total;
    private String orderCode;

    private List<InvoiceLineItem> lineItems;


}

package com.increff.pdf.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceLineItem {
    private Integer sno;
    private String productName;
    private String barcode;
    private Integer quantity;
    private Double unitPrice;
    private Double total;

}

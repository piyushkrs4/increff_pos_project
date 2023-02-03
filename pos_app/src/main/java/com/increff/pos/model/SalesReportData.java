package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SalesReportData {
    private String brand;
    private String category;
    private Integer quantity;
    private Double revenue;

    public SalesReportData(){
        quantity = 0;
        revenue = 0.0;
    }

}

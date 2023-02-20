package com.increff.pos.model.datas;

import lombok.Getter;
import lombok.Setter;

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

package com.increff.pos.model.datas;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailyReportData {
    private String date;
    private Integer totalOrders;
    private Integer totalItems;
    private Double totalRevenue;

    public DailyReportData(String formattedDate) {
        date = formattedDate;
        totalOrders = 0;
        totalItems = 0;
        totalRevenue = 0.0;
    }

    public DailyReportData() {

    }

}

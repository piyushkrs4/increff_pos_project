package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@Table(name = "daily_report_pojo")
public class DailyReportPojo extends AbstractPojo{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private ZonedDateTime reportDate;
    @Column(name = "total_order")
    private Integer totalOrders;
    @Column(name = "total_items")
    private Integer totalItems;
    @Column(name = "total_revenue")
    private Double totalRevenue;

}

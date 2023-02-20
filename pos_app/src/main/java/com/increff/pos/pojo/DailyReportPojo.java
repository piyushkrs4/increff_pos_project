package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
public class DailyReportPojo extends AbstractPojo{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private ZonedDateTime reportDate;
    @Column(nullable = false)
    private Integer totalOrders;
    @Column(nullable = false)
    private Integer totalItems;
    @Column(nullable = false)
    private Double totalRevenue;

}

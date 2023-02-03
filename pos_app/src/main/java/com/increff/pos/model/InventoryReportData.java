package com.increff.pos.model;

import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryReportData {
    private Integer brandId;
    private String brand;
    private String category;
    private Integer quantity;
}

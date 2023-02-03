package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class InventoryForm {
    @Size(min = 8, max = 8)
    private String barcode;
    @Min(0)
    @NotNull
    private Integer quantity;
}

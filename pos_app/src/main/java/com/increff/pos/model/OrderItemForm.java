package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class OrderItemForm {
    @Size(min = 8, max = 8)
    private String barcode;
    @Positive
    private Integer quantity;
    @Min(0)
    private Double sellingPrice;
}

// todo -> validate using tag

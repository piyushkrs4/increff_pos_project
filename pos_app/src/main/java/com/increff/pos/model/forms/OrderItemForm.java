package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class OrderItemForm {
    @NotBlank
    @Size(min = 8, max = 8)
    private String barcode;
    @NotNull
    @Positive
    @Max(200)
    private Integer quantity;
    @NotNull
    @Min(0)
    @Max(1000000)
    private Double sellingPrice;
}

// todo -> validate using tag

package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class InventoryForm {
    @NotBlank
    @Size(min = 8, max = 8)
    private String barcode;
    @NotNull
    @Min(0)
    private Integer quantity;
}

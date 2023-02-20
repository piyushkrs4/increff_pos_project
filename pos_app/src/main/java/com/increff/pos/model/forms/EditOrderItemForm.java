package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class EditOrderItemForm {
    @NotNull
    @Positive
    private Integer quantity;
    @NotNull
    @Min(0)
    private Double sellingPrice;
}

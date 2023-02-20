package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
public class EditProductForm {
    @NotBlank
    @Size(max = 15)
    private String name;
    @NotNull
    @Positive
    private Double mrp;
}

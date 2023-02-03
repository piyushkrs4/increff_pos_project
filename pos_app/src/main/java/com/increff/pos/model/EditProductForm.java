package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class EditProductForm {
    @NotBlank
    private String name;
    @Positive
    @NotNull
    private Double mrp;

    // todo -> add notnull/empty

}

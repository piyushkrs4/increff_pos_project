package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ProductForm {
    @Size(min = 8, max = 8)
    private String barcode;
    @NotBlank
    private String brand;
    @NotBlank
    private String category;
    @NotBlank
    private String name;
    @Min(0)
    @NotNull
    private Double mrp;

    // todo -> add notnull/empty

}

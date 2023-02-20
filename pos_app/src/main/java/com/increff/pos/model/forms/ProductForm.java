package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class ProductForm {
    @NotBlank
    @Size(min = 8, max = 8)
    private String barcode;
    @NotBlank
    @Size(max = 15)
    private String brand;
    @NotBlank
    @Size(max = 15)
    private String category;
    @NotBlank
    @Size(max = 15)
    private String name;
    @NotNull
    @Min(0)
    @Max(1000000)
    private Double mrp;

}

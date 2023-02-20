package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class BrandForm {
    @NotBlank
    @Size(max = 15)
    private String brand;
    @NotBlank
    @Size(max = 15)
    private String category;
}

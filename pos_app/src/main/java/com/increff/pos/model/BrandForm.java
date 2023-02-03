package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class BrandForm {
    @NotBlank
    private String brand;
    @NotBlank
    private String category;
}

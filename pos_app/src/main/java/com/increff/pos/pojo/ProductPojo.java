package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "product_pojo")
public class ProductPojo extends AbstractPojo{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String barcode;
    @Column(name = "brand_id")
    private Integer brandId;
    private String name;
    // todo -> change to primitive
    // todo -> make barcode unique notnull notblank
    // todo -> snakecase (naming str)
    private Double mrp;

}

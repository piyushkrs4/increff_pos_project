package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(
        uniqueConstraints = {@UniqueConstraint(name="barcode_uk",columnNames = {"barcode"})}
)
public class ProductPojo extends AbstractPojo{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String barcode;
    @Column(nullable = false)
    private Integer brandId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Double mrp;

}

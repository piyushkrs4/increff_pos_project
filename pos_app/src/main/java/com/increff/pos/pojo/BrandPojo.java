package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(
        uniqueConstraints = {@UniqueConstraint(name="brand_category_uk",columnNames = {"brand", "category"})}
)
public class BrandPojo extends AbstractPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String brand;
    @Column(nullable = false)
    private String category;
}

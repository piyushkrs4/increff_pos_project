package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(
        uniqueConstraints = {@UniqueConstraint(name="productId_uk",columnNames = {"productId"})}
)
public class InventoryPojo extends AbstractPojo {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private Integer productId;
    @Column(nullable = false)
    private Integer quantity;
}

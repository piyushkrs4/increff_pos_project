package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "inventory_pojo")
public class InventoryPojo extends AbstractPojo {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "product_id", unique = true)
    private Integer productId;
    private Integer quantity;
}

package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "order_item_pojo")
public class OrderItemPojo extends AbstractPojo{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "order_id")
    private Integer orderId;
    @Column(name = "product_id")
    private Integer productId;
    private Integer quantity;
    @Column(name = "selling_price")
    private Double sellingPrice;
}

package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class OrderPojo extends AbstractPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String orderCode;
    private Boolean orderStatus;

    public OrderPojo() {
        orderStatus = false;
    }
}

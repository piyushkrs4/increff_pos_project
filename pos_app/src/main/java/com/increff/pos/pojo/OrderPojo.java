package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

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

// todo -> abstract pojo -> created at updated at version
// todo -> zoned date time only
// todo -> add order code field

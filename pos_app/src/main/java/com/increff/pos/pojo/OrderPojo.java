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
//@Table(
//        uniqueConstraints = {@UniqueConstraint(name="orderCode_uk",columnNames = {"orderCode"})}
//)
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

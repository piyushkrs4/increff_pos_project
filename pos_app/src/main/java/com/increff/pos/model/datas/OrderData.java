package com.increff.pos.model.datas;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderData{
    private Integer id;
    private String datetime;
    private String orderCode;
    private Boolean status;

    public OrderData(){
        status = false;
    }
}

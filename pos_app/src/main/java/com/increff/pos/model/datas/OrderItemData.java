package com.increff.pos.model.datas;

import com.increff.pos.model.forms.OrderItemForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemData extends OrderItemForm {
    private Integer id;
    private Integer orderId;
    private String productName;
}

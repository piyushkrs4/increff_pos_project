package com.increff.pos.model.datas;

import com.increff.pos.model.forms.InventoryForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryData extends InventoryForm {
    private Integer id;
    private String productName;

}

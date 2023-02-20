package com.increff.pos.flow;

import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductFlow {
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;


    public Integer add(ProductPojo productPojo) throws ApiException, IllegalAccessException {
        Integer productId = productService.add(productPojo);
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(productPojo.getId());
        inventoryPojo.setQuantity(0);
        inventoryService.add(inventoryPojo);
        return productId;
    }
}

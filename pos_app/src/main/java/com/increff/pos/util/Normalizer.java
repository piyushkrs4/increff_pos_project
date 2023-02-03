package com.increff.pos.util;

import com.increff.pos.model.BrandForm;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.model.ProductForm;

public class Normalizer {

    public static void normalizeBrandForm(BrandForm brandForm) {
        brandForm.setBrand(StringUtil.toLowerCase(brandForm.getBrand()).trim());
        brandForm.setCategory(StringUtil.toLowerCase(brandForm.getCategory()).trim());
    }

    public static void normalizeProductForm(ProductForm productPojo) {
        productPojo.setBarcode(StringUtil.toLowerCase(productPojo.getBarcode()).trim());
        productPojo.setBrand(StringUtil.toLowerCase(productPojo.getBrand()).trim());
        productPojo.setCategory(StringUtil.toLowerCase(productPojo.getCategory()).trim());
        productPojo.setName(StringUtil.toLowerCase(productPojo.getName()).trim());
    }

    public static void normalizeInventoryForm(InventoryForm inventoryForm) {
        inventoryForm.setBarcode(StringUtil.toLowerCase(inventoryForm.getBarcode()).trim());
    }

    public static void normalizeOrderItemForm(OrderItemForm orderItemForm) {
        orderItemForm.setBarcode(StringUtil.toLowerCase(orderItemForm.getBarcode()).trim());
    }
}

package com.increff.pos.util;

import com.increff.pos.model.BrandForm;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.model.ProductForm;
import com.increff.pos.service.ApiException;

public class Validatorwa {

    public static void validateBrandForm(BrandForm brandForm) throws ApiException {
        if(StringUtil.isEmpty(brandForm.getBrand())) {
            throw new ApiException("Brand cannot be empty");
        }
        else if(StringUtil.isEmpty(brandForm.getCategory())) {
            throw new ApiException("Category cannot be empty");
        }
    }

    public static void validateProductForm(ProductForm productForm) throws ApiException {
        if(productForm.getBarcode().length() != 8)
            throw new ApiException("Please enter barcode of size 8");
        else if(StringUtil.isEmpty(productForm.getBrand()))
            throw new ApiException("Brand cannot be empty");
        else if(StringUtil.isEmpty(productForm.getCategory()))
            throw new ApiException("Category cannot be empty");
        else if(StringUtil.isEmpty(productForm.getName()))
            throw new ApiException("Name cannot be empty");
        else if(productForm.getMrp() < 0)
            throw new ApiException("MRP cannot be less than zero");
    }

    public static void validateInventoryForm(InventoryForm inventoryForm) throws ApiException {
        if(StringUtil.isEmpty(inventoryForm.getBarcode())) {
            throw new ApiException("Barcode cannot be empty");
        }
        else if(inventoryForm.getQuantity() <= 0) {
            throw new ApiException("Quantity cannot be less than one");
        }
    }

    public static void validateOrderItemForm(OrderItemForm orderItemForm) throws ApiException {
        if(StringUtil.isEmpty(orderItemForm.getBarcode())) {
            throw new ApiException("Barcode cannot be empty");
        }
        else if(orderItemForm.getQuantity() <= 0) {
            throw new ApiException("Quantity cannot be less than one");
        }
        else if(orderItemForm.getSellingPrice() < 0) {
            throw new ApiException("Selling price cannot be less than zero");
        }
    }

}

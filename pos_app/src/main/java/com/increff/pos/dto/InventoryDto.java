package com.increff.pos.dto;

import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.util.Converter.*;
import static com.increff.pos.util.Normalizer.normalizeInventoryForm;
import static com.increff.pos.util.NormalizerUtil.normalize;
import static com.increff.pos.util.ValidatorUtil.validate;
import static com.increff.pos.util.Validatorwa.validateInventoryForm;

@Component
public class InventoryDto {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ProductService productService;

    public void add(InventoryForm inventoryForm) throws ApiException, IllegalAccessException {
        validate(inventoryForm);
        normalize(inventoryForm);
        InventoryPojo inventoryPojo = convertGeneric(inventoryForm, InventoryPojo.class);
        inventoryPojo.setProductId(productService.getIdFromBarcode(inventoryForm.getBarcode()));
        inventoryService.add(inventoryPojo);
    }

    public List<InventoryData> getAll() throws ApiException {
        List<InventoryPojo> inventoryPojoList = inventoryService.getAll();
        List<InventoryData> inventoryDataList = new ArrayList<>();
        for (InventoryPojo inventoryPojo : inventoryPojoList) {
            ProductPojo productPojo = productService.get(inventoryPojo.getProductId());
            inventoryDataList.add(convertInventoryPojoToData(inventoryPojo, productPojo));
        }
        return inventoryDataList;
    }

    public void update(Integer inventoryId, InventoryForm inventoryForm) throws ApiException, IllegalAccessException {
        validate(inventoryForm);
        normalize(inventoryForm);
        InventoryPojo inventoryPojo = convertGeneric(inventoryForm, InventoryPojo.class);
        inventoryPojo.setProductId(productService.getIdFromBarcode(inventoryForm.getBarcode()));
        inventoryService.update(inventoryId, inventoryPojo);
    }
}

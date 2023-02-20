package com.increff.pos.dto;

import com.increff.pos.model.datas.InventoryReportData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.increff.pos.util.Converter.convertGeneric;

@Component
public class InventoryReportDto {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;

    public List<InventoryReportData> getAll() throws ApiException {
        List<InventoryPojo> inventoryPojoList = inventoryService.getAll();
        List<InventoryReportData> inventoryReportDataList = new ArrayList<>();
        HashMap<Integer, InventoryReportData> brandIdToInventoryReportDataMap = new HashMap<>();
        for (InventoryPojo inventoryPojo : inventoryPojoList) {
            InventoryReportData inventoryReportData = convertPojoToData(inventoryPojo);
            if (!brandIdToInventoryReportDataMap.containsKey(inventoryReportData.getBrandId())) {
                brandIdToInventoryReportDataMap.put(inventoryReportData.getBrandId(), inventoryReportData);
            } else {
                InventoryReportData mapInventoryReportData = brandIdToInventoryReportDataMap.get(inventoryReportData.getBrandId());
                mapInventoryReportData.setQuantity(mapInventoryReportData.getQuantity() + inventoryReportData.getQuantity());
            }
        }
        for (Map.Entry<Integer, InventoryReportData> map : brandIdToInventoryReportDataMap.entrySet()) {
            inventoryReportDataList.add(map.getValue());
        }
        return inventoryReportDataList;
    }

    private InventoryReportData convertPojoToData(InventoryPojo inventoryPojo) throws ApiException {
        ProductPojo productPojo = productService.get(inventoryPojo.getProductId());
        BrandPojo brandPojo = brandService.getBrand(productPojo.getBrandId());
        InventoryReportData inventoryReportData = convertGeneric(brandPojo, InventoryReportData.class);
        inventoryReportData.setQuantity(inventoryPojo.getQuantity());
        inventoryReportData.setBrandId(brandPojo.getId());
        return inventoryReportData;
    }

}

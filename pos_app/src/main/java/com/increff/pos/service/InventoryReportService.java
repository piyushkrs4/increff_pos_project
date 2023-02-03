package com.increff.pos.service;

import com.increff.pos.model.InventoryReportData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = ApiException.class)
public class InventoryReportService {

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
        BrandPojo brandPojo = brandService.get(productPojo.getBrandId());
        InventoryReportData inventoryReportData = new InventoryReportData();
        inventoryReportData.setBrand(brandPojo.getBrand());
        inventoryReportData.setCategory(brandPojo.getCategory());
        inventoryReportData.setQuantity(inventoryPojo.getQuantity());
        inventoryReportData.setBrandId(brandPojo.getId());
        return inventoryReportData;
    }

}

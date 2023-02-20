package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackFor = ApiException.class)
public class InventoryService {
    @Autowired
    private InventoryDao inventoryDao;

    public Integer add(InventoryPojo inventoryPojo) throws ApiException {
        InventoryPojo exInventoryPojo = getUsingProductId(inventoryPojo.getProductId());
        if (Objects.isNull(exInventoryPojo)) {
            inventoryDao.insert(inventoryPojo);
            return inventoryPojo.getId();
        } else {
            try {
                exInventoryPojo.setQuantity(Math.addExact(exInventoryPojo.getQuantity(), inventoryPojo.getQuantity()));

            } catch (Exception e) {
                throw new ApiException("Cannot add more than " + (Integer.MAX_VALUE - exInventoryPojo.getQuantity()) + " items!");
            }
            return exInventoryPojo.getId();
        }
    }

    public InventoryPojo get(Integer inventoryId) throws ApiException {
        return getCheck(inventoryId);
    }

    public List<InventoryPojo> getAll() {
        return inventoryDao.selectAll(InventoryPojo.class);
    }

    public void update(Integer inventoryId, InventoryPojo inventoryPojo) throws ApiException {
        InventoryPojo exInventoryPojo = getCheck(inventoryId);
        exInventoryPojo.setQuantity(inventoryPojo.getQuantity());
    }

    public InventoryPojo getUsingProductId(Integer productId) {
        return inventoryDao.selectUsingProductId(productId);
    }

    public void updateInventoryOnOrder(Integer quantity, ProductPojo productPojo) throws ApiException {
        InventoryPojo inventoryPojo = getUsingProductId(productPojo.getId());
        if (Objects.isNull(inventoryPojo))
            throw new ApiException("Item not available in inventory for barcode: " + productPojo.getBarcode());
        else if (inventoryPojo.getQuantity() < quantity)
            throw new ApiException("Cannot order more than " + inventoryPojo.getQuantity() + " items for barcode: " + productPojo.getBarcode());
        inventoryPojo.setQuantity(inventoryPojo.getQuantity() - quantity);
        update(inventoryPojo.getId(), inventoryPojo);
    }

    private InventoryPojo getCheck(Integer inventoryId) throws ApiException {
        InventoryPojo inventoryPojo = inventoryDao.select(inventoryId, InventoryPojo.class);
        if (Objects.isNull(inventoryPojo)) {
            throw new ApiException("Inventory with ID " + inventoryId + " does not exit!");
        }
        return inventoryPojo;
    }

}

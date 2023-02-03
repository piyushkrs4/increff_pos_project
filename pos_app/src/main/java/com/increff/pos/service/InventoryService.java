package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;
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

    public void add(InventoryPojo inventoryPojo) throws ApiException {
        InventoryPojo exInventoryPojo = getUsingProductId(inventoryPojo.getProductId());
        if (Objects.isNull(exInventoryPojo)) {
            inventoryDao.insert(inventoryPojo);
        } else {
            exInventoryPojo.setQuantity(exInventoryPojo.getQuantity() + inventoryPojo.getQuantity());
            inventoryDao.update(exInventoryPojo);
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
        inventoryDao.update(exInventoryPojo);
    }

    public InventoryPojo getUsingProductId(Integer productId) {
        InventoryPojo inventoryPojo = inventoryDao.selectUsingProductId(productId);
        return inventoryPojo;
    }

    private InventoryPojo getCheck(Integer inventoryId) throws ApiException {
        InventoryPojo inventoryPojo = inventoryDao.select(inventoryId, InventoryPojo.class);
        if (inventoryPojo == null) {
            throw new ApiException("Inventory with given ID does not exit, id: " + inventoryId);
        }
        return inventoryPojo;
    }

}

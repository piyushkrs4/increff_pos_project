package com.increff.pos.service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.EditProductForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = ApiException.class)
public class ProductService {
    @Autowired
    private ProductDao productDao;

    @Autowired
    private InventoryService inventoryService;

    public void add(ProductPojo productPojo) throws ApiException {
        try{
            productDao.insert(productPojo);
            InventoryPojo inventoryPojo = new InventoryPojo();
            inventoryPojo.setProductId(productPojo.getId());
            inventoryPojo.setQuantity(0);
            inventoryService.add(inventoryPojo);
        }catch(Exception e){
            throw new ApiException("Barcode already exist");
        }
    }

    public ProductPojo get(Integer productId) throws ApiException {
        return getCheck(productId);
    }

    public List<ProductPojo> getAll() {
        return productDao.selectAll(ProductPojo.class);
    }

    public void update(Integer productId, ProductPojo productPojo) throws ApiException {
        ProductPojo exProductPojo = getCheck(productId);
        exProductPojo.setName(productPojo.getName());
        exProductPojo.setMrp(productPojo.getMrp());
        productDao.update(exProductPojo);
    }

    public Integer getIdFromBarcode(String barcode) throws ApiException {
        ProductPojo productPojo = productDao.selectUsingBarcode(barcode);
        if (productPojo == null) {
            throw new ApiException("Product with given barcode does not exit, barcode: " + barcode);
        }
        return productPojo.getId();
    }

    private ProductPojo getCheck(Integer productId) throws ApiException {
        ProductPojo productPojo = productDao.select(productId, ProductPojo.class);
        if (productPojo == null) {
            throw new ApiException("Product with given ID does not exit, id: " + productId);
        }
        return productPojo;
    }

}

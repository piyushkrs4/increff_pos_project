package com.increff.pos.service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackFor = ApiException.class)
public class ProductService {
    @Autowired
    private ProductDao productDao;

    public Integer add(ProductPojo productPojo) throws ApiException {
        ProductPojo exProductPojo = productDao.selectUsingBarcode(productPojo.getBarcode());
        if (Objects.nonNull(exProductPojo))
            throw new ApiException("Barcode already exist!");
        productDao.insert(productPojo);
        return productPojo.getId();
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
    }

    public Integer getIdFromBarcode(String barcode) throws ApiException {
        ProductPojo productPojo = productDao.selectUsingBarcode(barcode);
        if (Objects.isNull(productPojo)) {
            throw new ApiException("Product with barcode: " + barcode + " does not exit!");
        }
        return productPojo.getId();
    }

    private ProductPojo getCheck(Integer productId) throws ApiException {
        ProductPojo productPojo = productDao.select(productId, ProductPojo.class);
        if (Objects.isNull(productPojo)) {
            throw new ApiException("Product with ID " + productId + " does not exit!");
        }
        return productPojo;
    }

}

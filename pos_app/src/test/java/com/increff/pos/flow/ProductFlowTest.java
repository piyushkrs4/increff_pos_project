package com.increff.pos.flow;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class ProductFlowTest extends AbstractUnitTest {
    @Autowired
    private ProductFlow productFlow;
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private InventoryService inventoryService;
    @Test
    public void testAdd() throws ApiException, IllegalAccessException {
        ProductPojo productPojo = createProductPojo(0);
        Integer productId = productFlow.add(productPojo);
        ProductPojo productPojoInDb = productService.get(productId);
        matchPojo(0, productPojoInDb);
        InventoryPojo inventoryPojo = inventoryService.getAll().get(0);
        assertEquals(productId, inventoryPojo.getProductId());
        assertEquals((Integer)0, inventoryPojo.getQuantity());
    }

    private void matchPojo(Integer id, ProductPojo productPojo) {
        assertEquals("barcode" + id, productPojo.getBarcode());
        assertEquals("product" + id, productPojo.getName());
        assertEquals((Double) 100.0, productPojo.getMrp());
    }

    private ProductPojo createProductPojo(Integer id) throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("brand" + id);
        brandPojo.setCategory("category" + id);
        brandService.addBrand(brandPojo);
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("barcode" + id);
        productPojo.setBrandId(brandService.findBrandCategoryId(brandPojo.getBrand(), brandPojo.getCategory()));
        productPojo.setName("product" + id);
        productPojo.setMrp(100.0);
        return productPojo;
    }
}

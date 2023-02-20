package com.increff.pos.service;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductServiceTest extends AbstractUnitTest {
    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductService productService;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testAdd() throws ApiException {
        ProductPojo productPojo = createProductPojo(0);
        Integer productId = productService.add(productPojo);
        ProductPojo productPojoInDb = productService.get(productId);
        matchPojo(0, productPojoInDb);
    }

    @Test
    public void testAddOnBarcodeAlreadyExist() throws ApiException {
        ProductPojo productPojo = createProductPojo(0);
        productService.add(productPojo);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Barcode already exist!");
        productService.add(productPojo);
    }

    @Test
    public void testGetOnWrongId() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Product with ID 0 does not exit!");
        productService.get(0);
    }

    @Test
    public void testGetAll() throws ApiException {
        List<ProductPojo> productPojoList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ProductPojo productPojo = createProductPojo(i);
            productService.add(productPojo);
        }
        productPojoList = productService.getAll();
        assertEquals(3, productPojoList.size());
        for (int i = 0; i < 3; i++) {
            matchPojo(i, productPojoList.get(i));
        }
    }

    @Test
    public void testUpdate() throws ApiException {
        ProductPojo productPojo = createProductPojo(0);
        Integer productId = productService.add(productPojo);
        productPojo.setName("new product");
        productPojo.setMrp(1.0);
        productService.update(productId, productPojo);
        assertEquals("new product", productPojo.getName());
        assertEquals((Double) 1.0, productPojo.getMrp());
    }

    @Test
    public void testGetIdFromBarcode() throws ApiException {
        ProductPojo productPojo = createProductPojo(0);
        Integer productId = productService.add(productPojo);
        assertEquals(productId, productService.getIdFromBarcode("barcode0"));
    }

    @Test
    public void testGetIdFromIncorrectBarcode() throws ApiException {
        ProductPojo productPojo = createProductPojo(0);
        Integer productId = productService.add(productPojo);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Product with barcode: wrong barcode does not exit!");
        productService.getIdFromBarcode("wrong barcode");
    }

    private void matchPojo(Integer id, ProductPojo productPojo) throws ApiException {
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

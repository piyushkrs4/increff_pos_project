package com.increff.pos.service;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class InventoryServiceTest extends AbstractUnitTest {
    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testAdd() throws ApiException {
        InventoryPojo inventoryPojo = createInventoryPojo(0);
        Integer inventoryId = inventoryService.add(inventoryPojo);
        matchPojo(0, inventoryService.getAll().get(0));
    }

    @Test
    public void testAddOnExistingInventory() throws ApiException {
        InventoryPojo inventoryPojo = createInventoryPojo(0);
        Integer inventoryId = inventoryService.add(inventoryPojo);
        inventoryPojo.setQuantity(400);
        inventoryService.add(inventoryPojo);
        assertEquals((Integer) 800, inventoryService.get(inventoryId).getQuantity());
    }

    @Test
    public void testUpdate() throws ApiException {
        InventoryPojo inventoryPojo = createInventoryPojo(0);
        Integer inventoryId = inventoryService.add(inventoryPojo);
        inventoryPojo.setQuantity(600);
        inventoryService.update(inventoryId, inventoryPojo);
        assertEquals((Integer) 600, inventoryService.get(inventoryId).getQuantity());
    }

    @Test
    public void testGetInvalidId() throws ApiException {
        exceptionRule.expect(ApiException.class);
        inventoryService.get(-1);
    }


    @Test
    public void testUpdateInventoryOnOrder() throws ApiException {
        InventoryPojo inventoryPojo = createInventoryPojo(0);
        inventoryService.add(inventoryPojo);
        ProductPojo productPojo = productService.get(productService.getIdFromBarcode("barcode0"));
        inventoryService.updateInventoryOnOrder(100, productPojo);
        assertEquals((Integer) 300, inventoryService.getAll().get(0).getQuantity());
    }

    @Test
    public void testUpdateInventoryOnOrderInsufficientQuantity() throws ApiException {
        InventoryPojo inventoryPojo = createInventoryPojo(0);
        inventoryService.add(inventoryPojo);
        ProductPojo productPojo = productService.get(productService.getIdFromBarcode("barcode0"));
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Cannot order more than " + inventoryPojo.getQuantity() + " items for barcode: " + productPojo.getBarcode());
        inventoryService.updateInventoryOnOrder(500, productPojo);
    }



    private void matchPojo(Integer id, InventoryPojo inventoryPojo) throws ApiException {
        ProductPojo productPojo = productService.get(inventoryPojo.getProductId());
        assertEquals("barcode" + id, productPojo.getBarcode());
        assertEquals((Integer) 400, inventoryPojo.getQuantity());
    }

    private InventoryPojo createInventoryPojo(Integer id) throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("brand" + id);
        brandPojo.setCategory("category" + id);
        brandService.addBrand(brandPojo);

        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("barcode" + id);
        productPojo.setBrandId(brandService.findBrandCategoryId(brandPojo.getBrand(), brandPojo.getCategory()));
        productPojo.setName("product" + id);
        productPojo.setMrp(100.0);
        Integer productId = productService.add(productPojo);

        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(productId);
        inventoryPojo.setQuantity(400);

        return inventoryPojo;
    }

}

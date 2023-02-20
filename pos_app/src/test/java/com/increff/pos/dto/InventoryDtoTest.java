package com.increff.pos.dto;

import com.increff.pos.model.datas.InventoryData;
import com.increff.pos.model.forms.BrandForm;
import com.increff.pos.model.forms.InventoryForm;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class InventoryDtoTest extends AbstractUnitTest {

    @Autowired
    private BrandDto brandDto;
    @Autowired
    private ProductDto productDto;
    @Autowired
    private InventoryDto inventoryDto;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testAdd() throws ApiException, IllegalAccessException {
        InventoryForm inventoryForm = createInventory(0);
        inventoryDto.add(inventoryForm);

        InventoryData inventoryData = inventoryDto.getAll().get(0);
        matchData(0, inventoryData);
    }

    @Test
    public void testUnavailableBarcodeOnAdd() throws ApiException, IllegalAccessException {
        InventoryForm inventoryForm = createInventory(0);
        inventoryForm.setBarcode("barcode1");

        exceptionRule .expect(ApiException.class);
        exceptionRule.expectMessage("Product with barcode: barcode1 does not exit!");
        inventoryDto.add(inventoryForm);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testIncorrectBarcodeSizeOnAdd() throws ApiException, IllegalAccessException {
        InventoryForm inventoryForm = createInventory(0);
        inventoryForm.setBarcode("barcode");
        inventoryDto.add(inventoryForm);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testNegativeQuantityOnAdd() throws ApiException, IllegalAccessException {
        InventoryForm inventoryForm = createInventory(0);
        inventoryForm.setQuantity(-1);
        inventoryDto.add(inventoryForm);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testEmptyBarcodeOnAdd() throws ApiException, IllegalAccessException {
        InventoryForm inventoryForm = createInventory(0);
        inventoryForm.setBarcode("");
        inventoryDto.add(inventoryForm);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testEmptyQuantityOnAdd() throws ApiException, IllegalAccessException {
        InventoryForm inventoryForm = createInventory(0);
        inventoryForm.setQuantity(null);
        inventoryDto.add(inventoryForm);
    }

    @Test
    public void testGetAll() throws ApiException, IllegalAccessException {
        for (Integer index = 0; index < 5; index++) {
            InventoryForm inventoryForm = createInventory(index);
            inventoryDto.add(inventoryForm);
        }
        List<InventoryData> inventoryDataList = inventoryDto.getAll();
        assertEquals(5, inventoryDataList.size());
        for (Integer index = 0; index < 5; index++) {
            matchData(index, inventoryDataList.get(index));
        }
    }

    @Test
    public void testUpdate() throws ApiException, IllegalAccessException {
        InventoryForm inventoryForm = createInventory(0);
        Integer inventoryId = inventoryDto.add(inventoryForm);

        inventoryForm.setQuantity(115);
        inventoryDto.update(inventoryId, inventoryForm);

        InventoryData inventoryData = inventoryDto.getAll().get(0);
        assertEquals((Integer) 115, inventoryData.getQuantity());
    }

    @Test(expected = ConstraintViolationException.class)
    public void testNegativeQuantityOnUpdate() throws ApiException, IllegalAccessException {
        InventoryForm inventoryForm = createInventory(0);
        Integer inventoryId = inventoryDto.add(inventoryForm);

        inventoryForm.setQuantity(-1);
        inventoryDto.update(inventoryId, inventoryForm);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testEmptyQuantityOnUpdate() throws ApiException, IllegalAccessException {
        InventoryForm inventoryForm = createInventory(0);
        Integer inventoryId = inventoryDto.add(inventoryForm);

        inventoryForm.setQuantity(null);
        inventoryDto.update(inventoryId, inventoryForm);
    }


    private void matchData(Integer id, InventoryData inventoryData) {
        assertEquals("product" + id, inventoryData.getProductName());
        assertEquals("barcode" + id, inventoryData.getBarcode());
        assertEquals((Integer) 100, inventoryData.getQuantity());
    }

    private InventoryForm createInventory(Integer id) throws ApiException, IllegalAccessException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("brand" + id);
        brandForm.setCategory("category" + id);
        brandDto.addBrand(brandForm);

        ProductForm productForm = new ProductForm();
        productForm.setBarcode("barcode" + id);
        productForm.setBrand("brand" + id);
        productForm.setCategory("category" + id);
        productForm.setName("product" + id);
        productForm.setMrp(100.0);
        productDto.add(productForm);

        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("barcode" + id);
        inventoryForm.setQuantity(100);

        return inventoryForm;
    }

}

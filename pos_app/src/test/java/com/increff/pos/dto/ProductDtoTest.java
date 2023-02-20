package com.increff.pos.dto;

import com.increff.pos.model.datas.ProductData;
import com.increff.pos.model.forms.BrandForm;
import com.increff.pos.model.forms.EditProductForm;
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

public class ProductDtoTest extends AbstractUnitTest {
    @Autowired
    private BrandDto brandDto;
    @Autowired
    private ProductDto productDto;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();


    @Test
    public void testAdd() throws ApiException, IllegalAccessException {
        ProductForm productForm = createProduct(0);
        productDto.add(productForm);

        ProductData productData = productDto.getAll().get(0);
        matchData(0, productData);
    }

    @Test
    public void testUniqueBarcodeOnAdd() throws ApiException, IllegalAccessException {
        ProductForm productForm = createProduct(0);
        productDto.add(productForm);

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Barcode already exist!");
        productDto.add(productForm);
    }

    @Test
    public void testUnavailableBrandCategoryPairOnAdd() throws ApiException, IllegalAccessException {
        ProductForm productForm = createProduct(0);
        productDto.add(productForm);

        productForm.setBrand("brand");
        productForm.setCategory("category");

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("brand, category pair does not exist");
        productDto.add(productForm);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testEmptyBarcodeOnAdd() throws ApiException, IllegalAccessException {
        ProductForm productForm = createProduct(0);
        productForm.setBarcode("");
        productDto.add(productForm);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testEmptyBrandOnAdd() throws ApiException, IllegalAccessException {
        ProductForm productForm = createProduct(0);
        productForm.setBrand("");
        productDto.add(productForm);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testEmptyCategoryOnAdd() throws ApiException, IllegalAccessException {
        ProductForm productForm = createProduct(0);
        productForm.setCategory("");
        productDto.add(productForm);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testEmptyProductNameOnAdd() throws ApiException, IllegalAccessException {
        ProductForm productForm = createProduct(0);
        productForm.setName("");
        productDto.add(productForm);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testEmptyMrpOnAdd() throws ApiException, IllegalAccessException {
        ProductForm productForm = createProduct(0);
        productForm.setMrp(null);
        productDto.add(productForm);
    }

    @Test
    public void testGetAll() throws ApiException, IllegalAccessException {
        for(Integer index = 0; index < 5; index++) {
            ProductForm productForm = createProduct(index);
            productDto.add(productForm);
        }
        List<ProductData> productDataList = productDto.getAll();
        assertEquals(5, productDataList.size());
        for(Integer index = 0; index < 5; index++) {
            matchData(index, productDataList.get(index));
        }
    }

    @Test
    public void testUpdate() throws ApiException, IllegalAccessException {
        ProductForm productForm = createProduct(0);
        Integer productId = productDto.add(productForm);

        EditProductForm editProductForm = new EditProductForm();
        editProductForm.setName("new product");
        editProductForm.setMrp(200.0);
        productDto.update(productId, editProductForm);

        ProductData productData = productDto.getAll().get(0);
        assertEquals("new product", productData.getName());
        assertEquals((Double) 200.0, productData.getMrp());
    }

    @Test(expected = ConstraintViolationException.class)
    public void testEmptyProductNameOnUpdate() throws ApiException, IllegalAccessException {
        ProductForm productForm = createProduct(0);
        Integer productId = productDto.add(productForm);
        EditProductForm editProductForm = new EditProductForm();
        editProductForm.setName("");
        editProductForm.setMrp(200.0);
        productDto.update(productId, editProductForm);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testEmptyMrpOnUpdate() throws ApiException, IllegalAccessException {
        ProductForm productForm = createProduct(0);
        Integer productId = productDto.add(productForm);
        EditProductForm editProductForm = new EditProductForm();
        editProductForm.setName("new name");
        editProductForm.setMrp(null);
        productDto.update(productId, editProductForm);
    }

    private void matchData(Integer id, ProductData productData){
        assertEquals("barcode" + id, productData.getBarcode());
        assertEquals("brand" + id, productData.getBrand());
        assertEquals("category" + id, productData.getCategory());
        assertEquals("product" + id, productData.getName());
    }

    private ProductForm createProduct(Integer id) throws ApiException, IllegalAccessException {
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
        return productForm;
    }

}

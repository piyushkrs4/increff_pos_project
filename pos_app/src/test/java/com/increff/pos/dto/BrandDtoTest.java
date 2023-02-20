package com.increff.pos.dto;

import com.increff.pos.model.datas.BrandData;
import com.increff.pos.model.forms.BrandForm;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BrandDtoTest extends AbstractUnitTest {
    @Autowired
    private BrandDto brandDto;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testAddBrand() throws ApiException, IllegalAccessException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("Test Brand");
        brandForm.setCategory("teSt catEgOrY");
        brandDto.addBrand(brandForm);
        BrandData brandData = brandDto.getAllBrands().get(0);
        assertEquals("test brand", brandData.getBrand());
        assertEquals("test category", brandData.getCategory());
    }

    @Test
    public void testBrandCategoryUniquenessOnAdd() throws ApiException, IllegalAccessException {
        BrandForm brandForm = new BrandForm();
        String brand = "brand";
        String category = "category";
        brandForm.setBrand(brand);
        brandForm.setCategory(category);
        brandDto.addBrand(brandForm);

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Brand: " + brand + " and category: " + category + " pair already exist!");
        brandDto.addBrand(brandForm);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testEmptyBrandOnAdd() throws ApiException, IllegalAccessException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("");
        brandForm.setCategory("category");
        brandDto.addBrand(brandForm);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testEmptyCategoryOnAdd() throws ApiException, IllegalAccessException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("brand");
        brandForm.setCategory("");
        brandDto.addBrand(brandForm);
    }


    @Test
    public void testGetAll() throws ApiException, IllegalAccessException {
        BrandForm brandForm = new BrandForm();
        for (Integer i = 0; i < 10; i++) {
            brandForm.setBrand("Brand_" + i);
            brandForm.setCategory("Category_" + i);
            brandDto.addBrand(brandForm);
        }
        List<BrandData> brandDataList = brandDto.getAllBrands();
        assertEquals(10, brandDataList.size());
        for (Integer i = 0; i < 10; i++) {
            assertEquals("brand_" + i, brandDataList.get(i).getBrand());
            assertEquals("category_" + i, brandDataList.get(i).getCategory());
        }
    }


    @Test
    public void testUpdate() throws ApiException, IllegalAccessException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("Brand");
        brandForm.setCategory("Category");
        Integer brandId = brandDto.addBrand(brandForm);
        brandForm.setBrand("new Brand");
        brandForm.setCategory("new Category");
        brandDto.updateBrand(brandId, brandForm);
        brandForm = brandDto.getAllBrands().get(0);
        assertEquals("new brand", brandForm.getBrand());
        assertEquals("new category", brandForm.getCategory());
    }
}

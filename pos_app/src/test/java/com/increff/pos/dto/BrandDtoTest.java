package com.increff.pos.dto;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BrandDtoTest extends AbstractUnitTest {
    @Autowired
    private BrandDto brandDto;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testAdd() throws ApiException, IllegalAccessException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("   Test Brand   ");
        brandForm.setCategory(" teSt catEgOrY ");
        brandDto.add(brandForm);
        BrandData brandData = brandDto.getAll().get(0);
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
        brandDto.add(brandForm);

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Brand: " + brand + " and category: " + category + " pair already exist!");
        brandDto.add(brandForm);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testEmptyBrandOnAdd() throws ApiException, IllegalAccessException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("");
        brandForm.setCategory("category");

//        exceptionRule.expect(ConstraintViolationException.class);
//        exceptionRule.expectMessage("[Brand must not be blank]");
        brandDto.add(brandForm);
    }

    @Test
    public void testGetAll() throws ApiException, IllegalAccessException {
        BrandForm brandForm = new BrandForm();
        for(Integer i = 0; i < 10; i++){
            brandForm.setBrand("Brand_" + i);
            brandForm.setCategory("Category_" + i);
            brandDto.add(brandForm);
        }
        assertEquals(10, brandDto.getAll().size());
        List<BrandData> brandDataList = new ArrayList<>();
        for(Integer i = 0; i < 10; i++){
            assertEquals("brand_" + i, brandDataList.get(i).getBrand());
            assertEquals("category_" + i, brandDataList.get(i).getCategory());
        }
    }


    @Test
    public void update() throws ApiException, IllegalAccessException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("Apple");
        brandForm.setCategory("headphone");
        Integer brandId = brandDto.add(brandForm);
        brandForm.setCategory("mobile");
        brandDto.update(brandId, brandForm);
        brandForm = brandDto.getAll().get(0);
        assertEquals("apple", brandForm.getBrand());
        assertEquals("mobile", brandForm.getCategory());
    }
}

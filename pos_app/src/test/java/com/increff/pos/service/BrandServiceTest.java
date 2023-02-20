package com.increff.pos.service;

import com.increff.pos.pojo.BrandPojo;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class BrandServiceTest extends AbstractUnitTest {

    @Autowired
    private BrandService brandService;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testAdd() throws ApiException {
        BrandPojo brandPojo = createBrandPojo(0);
        Integer brandId = brandService.addBrand(brandPojo);
        BrandPojo brandPojoInDb = brandService.getBrand(brandId);
        matchPojo(0, brandPojoInDb);
    }


    @Test
    public void testGetAll() throws ApiException {
        for(int i = 0; i < 5; i++){
            BrandPojo brandPojo = createBrandPojo(i);
            brandService.addBrand(brandPojo);
        }
        List<BrandPojo> brandPojoList = brandService.getAllBrands();
        assertEquals(5, brandPojoList.size());
        for(int i = 0; i < 5; i++){
            matchPojo(i, brandPojoList.get(i));
        }
    }

    @Test
    public void testUpdate() throws ApiException {
        BrandPojo brandPojo = createBrandPojo(0);
        Integer brandId = brandService.addBrand(brandPojo);
        BrandPojo newBrandPojo = createBrandPojo(1);
        brandService.updateBrand(brandId, newBrandPojo);
        matchPojo(1, newBrandPojo);
    }

    @Test
    public void testFindBrandCategoryId() throws ApiException {
        BrandPojo brandPojo = createBrandPojo(0);
        Integer brandId = brandService.addBrand(brandPojo);
        Integer id = brandService.findBrandCategoryId("brand0", "category0");
        assertEquals(brandId, id);
    }

    @Test
    public void testFindBrandCategoryIdForIncorrectPair() throws ApiException {
        BrandPojo brandPojo = createBrandPojo(0);
        brandService.addBrand(brandPojo);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("brand0, category1 pair does not exist");
        brandService.findBrandCategoryId("brand0", "category1");
    }


    private void matchPojo(Integer id, BrandPojo brandPojo){
        assertEquals("brand" + id, brandPojo.getBrand());
        assertEquals("category" + id, brandPojo.getCategory());
    }

    private BrandPojo createBrandPojo(Integer id){
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("brand" + id);
        brandPojo.setCategory("category" + id);
        return brandPojo;
    }

}

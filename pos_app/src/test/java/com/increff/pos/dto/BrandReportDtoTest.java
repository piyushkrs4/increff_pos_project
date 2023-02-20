package com.increff.pos.dto;

import com.increff.pos.model.datas.BrandData;
import com.increff.pos.model.forms.BrandForm;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class BrandReportDtoTest extends AbstractUnitTest {
    @Autowired
    private BrandDto brandDto;
    @Autowired
    private BrandReportDto brandReportDto;

    @Test
    public void testGetAllBrands() throws ApiException, IllegalAccessException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("Test Brand");
        brandForm.setCategory("teSt catEgOrY");
        brandDto.addBrand(brandForm);
        BrandData brandData = brandReportDto.getAllBrands().get(0);
        assertEquals("test brand", brandData.getBrand());
        assertEquals("test category", brandData.getCategory());
    }


}

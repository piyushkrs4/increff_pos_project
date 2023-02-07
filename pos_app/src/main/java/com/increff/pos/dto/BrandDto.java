package com.increff.pos.dto;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.util.Converter.convertGeneric;
import static com.increff.pos.util.NormalizerUtil.normalize;
import static com.increff.pos.util.ValidatorUtil.validate;

@Component
public class BrandDto {
    @Autowired
    private BrandService brandService;

    public Integer add(BrandForm brandForm) throws ApiException, IllegalAccessException {
        validate(brandForm);
        normalize(brandForm);
        BrandPojo brandPojo = convertGeneric(brandForm, BrandPojo.class);
        return brandService.add(brandPojo);
    }

    public List<BrandData> getAll() throws ApiException {
        List<BrandPojo> brandPojoList = brandService.getAll();
        List<BrandData> brandDataList = new ArrayList<>();
        for (BrandPojo brandPojo : brandPojoList) {
            brandDataList.add(convertGeneric(brandPojo, BrandData.class));
        }
        return brandDataList;
    }

    public void update(Integer brandId, BrandForm brandForm) throws ApiException, IllegalAccessException {
        validate(brandForm);
        normalize(brandForm);
        BrandPojo brandPojo = convertGeneric(brandForm, BrandPojo.class);
        brandService.update(brandId, brandPojo);
    }
}

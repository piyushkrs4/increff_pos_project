package com.increff.pos.dto;

import com.increff.pos.model.datas.BrandData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.util.Converter.convertGeneric;

@Component
public class BrandReportDto {
    @Autowired
    private BrandService brandService;

    public List<BrandData> getAllBrands() throws ApiException {
        List<BrandPojo> brandPojoList = brandService.getAllBrands();
        List<BrandData> brandDataList = new ArrayList<>();
        for (BrandPojo brandPojo : brandPojoList) {
            brandDataList.add(convertGeneric(brandPojo, BrandData.class));
        }
        return brandDataList;
    }
}

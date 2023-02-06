package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(rollbackFor = ApiException.class)
public class BrandService {
    @Autowired
    private BrandDao brandDao;

    public Integer add(BrandPojo brandPojo) throws ApiException {
        checkBrandCategoryExistence(brandPojo.getBrand(), brandPojo.getCategory());
        brandDao.insert(brandPojo);
        return brandPojo.getId();
    }

    public BrandPojo get(Integer brandId) throws ApiException {
        return getCheck(brandId);
    }

    public List<BrandPojo> getAll() {
        return brandDao.selectAll(BrandPojo.class);
    }


    public void update(Integer brandId, BrandPojo brandPojo) throws ApiException {
        checkBrandCategoryExistence(brandPojo.getBrand(), brandPojo.getCategory());
        BrandPojo exBrandPojo = getCheck(brandId);
        exBrandPojo.setBrand(brandPojo.getBrand());
        exBrandPojo.setCategory(brandPojo.getCategory());
        brandDao.update(exBrandPojo);
    }

    public Integer findBrandCategoryId(String brand, String category){
        BrandPojo brandPojo = brandDao.selectBrandCategory(brand, category);
        return brandPojo.getId();
    }

    private BrandPojo getCheck(Integer brandId) throws ApiException {
        BrandPojo brandPojo = brandDao.select(brandId, BrandPojo.class);
        if (Objects.isNull(brandPojo)) {
            throw new ApiException("Brand with given ID does not exit, id: " + brandId);
        }
        return brandPojo;
    }

    private void checkBrandCategoryExistence(String brand, String category) throws ApiException {
        BrandPojo exBrandPojo = brandDao.selectBrandCategory(brand, category);
        if(!Objects.isNull(exBrandPojo)){
            throw new ApiException("Brand: " + brand + " and category: " + category + " pair already exist!");
        }
    }

}

// Todo -> error message user friendly

package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackFor = ApiException.class)
public class BrandService {
    @Autowired
    private BrandDao brandDao;

    public Integer addBrand(BrandPojo brandPojo) throws ApiException {
        checkBrandCategoryExistence(brandPojo.getBrand(), brandPojo.getCategory());
        brandDao.insert(brandPojo);
        return brandPojo.getId();
    }

    public BrandPojo getBrand(Integer brandId) throws ApiException {
        return getBrandCheck(brandId);
    }

    public List<BrandPojo> getAllBrands() {
        return brandDao.selectAll(BrandPojo.class);
    }


    public void updateBrand(Integer brandId, BrandPojo brandPojo) throws ApiException {
        checkBrandCategoryExistence(brandPojo.getBrand(), brandPojo.getCategory());
        BrandPojo exBrandPojo = getBrandCheck(brandId);
        exBrandPojo.setBrand(brandPojo.getBrand());
        exBrandPojo.setCategory(brandPojo.getCategory());
    }

    public Integer findBrandCategoryId(String brand, String category) throws ApiException {
        BrandPojo brandPojo = brandDao.selectBrandCategory(brand, category);
        if (Objects.isNull(brandPojo)) {
            throw new ApiException(brand + ", " + category + " pair does not exist!");
        }
        return brandPojo.getId();
    }

    private BrandPojo getBrandCheck(Integer brandId) throws ApiException {
        BrandPojo brandPojo = brandDao.select(brandId, BrandPojo.class);
        if (Objects.isNull(brandPojo)) {
            throw new ApiException("Brand with ID " + brandId + " does not exit!");
        }
        return brandPojo;
    }

    private void checkBrandCategoryExistence(String brand, String category) throws ApiException {
        BrandPojo exBrandPojo = brandDao.selectBrandCategory(brand, category);
        if (Objects.nonNull(exBrandPojo)) {
            throw new ApiException("Brand: " + brand + " and category: " + category + " pair already exist!");
        }
    }

}

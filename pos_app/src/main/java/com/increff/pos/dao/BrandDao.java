package com.increff.pos.dao;

import com.increff.pos.pojo.BrandPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class BrandDao extends AbstractDao {
    private static final String select_brand_category = "select p from BrandPojo p where brand=:brand and category=:category";

    public BrandPojo selectBrandCategory(String brand, String category) {
        TypedQuery<BrandPojo> selectBrandCategoryQuery = getQuery(select_brand_category, BrandPojo.class);
        selectBrandCategoryQuery.setParameter("brand", brand);
        selectBrandCategoryQuery.setParameter("category", category);
        return getSingle(selectBrandCategoryQuery);
    }

}

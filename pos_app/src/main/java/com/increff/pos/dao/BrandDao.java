package com.increff.pos.dao;

import com.increff.pos.pojo.BrandPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class BrandDao extends AbstractDao{
    private static String select_brand_category = "select p from BrandPojo p where brand=:brand and category=:category";
    
    @PersistenceContext
    private EntityManager em;

    public BrandPojo selectBrandCategory(String brand, String category){
        TypedQuery<BrandPojo> selectBrandCategoryQuery = getQuery(select_brand_category, BrandPojo.class);
        selectBrandCategoryQuery.setParameter("brand", brand);
        selectBrandCategoryQuery.setParameter("category", category);
        return getSingle(selectBrandCategoryQuery);
    }

}

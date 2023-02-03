package com.increff.pos.dao;

import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class ProductDao extends AbstractDao{
    private static String select_barcode = "select p from ProductPojo p where barcode=:barcode";
    
    @PersistenceContext
    private EntityManager em;

    public ProductPojo selectUsingBarcode(String barcode){
        TypedQuery<ProductPojo> query = getQuery(select_barcode, ProductPojo.class);
        query.setParameter("barcode", barcode);
        return getSingle(query);
    }

    //todo -> remove delete
    //todo -> generic method for insert, select

}

package com.increff.pos.dao;

import com.increff.pos.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class ProductDao extends AbstractDao {
    private static final String select_barcode = "select p from ProductPojo p where barcode=:barcode";

    public ProductPojo selectUsingBarcode(String barcode) {
        TypedQuery<ProductPojo> query = getQuery(select_barcode, ProductPojo.class);
        query.setParameter("barcode", barcode);
        return getSingle(query);
    }

}

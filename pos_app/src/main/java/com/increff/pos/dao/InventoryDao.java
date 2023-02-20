package com.increff.pos.dao;

import com.increff.pos.pojo.InventoryPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class InventoryDao extends AbstractDao {
    private static final String select_pid = "select p from InventoryPojo p where productId=:productId";

    public InventoryPojo selectUsingProductId(Integer productId) {
        TypedQuery<InventoryPojo> query = getQuery(select_pid, InventoryPojo.class);
        query.setParameter("productId", productId);
        return getSingle(query);
    }
}

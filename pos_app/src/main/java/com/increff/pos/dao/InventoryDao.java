package com.increff.pos.dao;

import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.service.ApiException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class InventoryDao extends AbstractDao{
    private static String select_pid = "select p from InventoryPojo p where productId=:productId";

    @PersistenceContext
    private EntityManager em;

    public InventoryPojo selectUsingProductId(Integer productId){
        TypedQuery<InventoryPojo> query = getQuery(select_pid, InventoryPojo.class);
        query.setParameter("productId", productId);
        return getSingle(query);
    }
}

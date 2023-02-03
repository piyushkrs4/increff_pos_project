package com.increff.pos.dao;

import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class OrderItemDao extends AbstractDao {
    private static String select_pid_oid = "select p from OrderItemPojo p where productId=:productId and orderId=:orderId";
    private static String select_orderId = "select p from OrderItemPojo p where orderId=:orderId";

    @PersistenceContext
    private EntityManager em;

    public List<OrderItemPojo> selectByOrderId(Integer orderId) {
        TypedQuery<OrderItemPojo> query = getQuery(select_orderId, OrderItemPojo.class);
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }

    public OrderItemPojo selectUsingProductIdAndOrderId(Integer productId, Integer orderId) {
        TypedQuery<OrderItemPojo> query = getQuery(select_pid_oid, OrderItemPojo.class);
        query.setParameter("productId", productId);
        query.setParameter("orderId", orderId);
        return getSingle(query);
    }

}

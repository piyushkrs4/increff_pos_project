package com.increff.pos.dao;

import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class OrderItemDao extends AbstractDao {
    private static final String select_orderId = "select p from OrderItemPojo p where orderId=:orderId";

    public List<OrderItemPojo> selectByOrderId(Integer orderId) {
        TypedQuery<OrderItemPojo> query = getQuery(select_orderId, OrderItemPojo.class);
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }

}

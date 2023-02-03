package com.increff.pos.dao;

import com.increff.pos.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public class OrderDao extends AbstractDao {
    private static String select_all_placed = "select p from OrderPojo p where orderStatus = true";
    private static String select_filtered = "select p from OrderPojo p where updatedAt >=: startDateTime and updatedAt <=: endDateTime and orderStatus = true";

    @PersistenceContext
    private EntityManager em;

    public List<OrderPojo> selectAllPlaced() {
        TypedQuery<OrderPojo> query = getQuery(select_all_placed, OrderPojo.class);
        return query.getResultList();
    }

    public List<OrderPojo> selectFilteredOrder(ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        TypedQuery<OrderPojo> query = getQuery(select_filtered, OrderPojo.class);
        query.setParameter("startDateTime", startDateTime);
        query.setParameter("endDateTime", endDateTime);
        return query.getResultList();
    }

}

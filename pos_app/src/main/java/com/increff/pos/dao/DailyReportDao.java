package com.increff.pos.dao;

import com.increff.pos.pojo.DailyReportPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public class DailyReportDao extends AbstractDao{
    private static String select_filtered = "select p from DailyReportPojo p where reportDate >=: startDateTime and reportDate <=: endDateTime";
    @PersistenceContext
    private EntityManager em;

    public List<DailyReportPojo> selectFilteredReport(ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        TypedQuery<DailyReportPojo> query = getQuery(select_filtered, DailyReportPojo.class);
        query.setParameter("startDateTime", startDateTime);
        query.setParameter("endDateTime", endDateTime);
        return query.getResultList();
    }

}

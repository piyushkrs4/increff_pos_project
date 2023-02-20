package com.increff.pos.dao;

import com.increff.pos.pojo.DailyReportPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public class DailyReportDao extends AbstractDao{
    private static final String select_filtered = "select p from DailyReportPojo p where reportDate between : startDateTime and : endDateTime";

    public List<DailyReportPojo> selectFilteredReport(ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        TypedQuery<DailyReportPojo> query = getQuery(select_filtered, DailyReportPojo.class);
        query.setParameter("startDateTime", startDateTime);
        query.setParameter("endDateTime", endDateTime);
        return query.getResultList();
    }

}

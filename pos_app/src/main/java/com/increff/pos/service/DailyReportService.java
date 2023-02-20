package com.increff.pos.service;

import com.increff.pos.dao.DailyReportDao;
import com.increff.pos.model.forms.DailyReportForm;
import com.increff.pos.pojo.DailyReportPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

import static com.increff.pos.util.DateTimeFormat.END_TIME;
import static com.increff.pos.util.DateTimeFormat.START_TIME;

@Service
@Transactional(rollbackFor = ApiException.class)
public class DailyReportService {
    @Autowired
    private DailyReportDao dailyReportDao;

    public void add(DailyReportPojo dailyReportPojo) {
        dailyReportDao.insert(dailyReportPojo);
    }

    public List<DailyReportPojo> getAll() throws ApiException {
        List<DailyReportPojo> dailyReportPojoList = dailyReportDao.selectAll(DailyReportPojo.class);
        dailyReportPojoList.sort((dailyReportPojo1, dailyReportPojo2) -> dailyReportPojo2.getReportDate().compareTo(dailyReportPojo1.getReportDate()));
        return dailyReportPojoList;
    }

    public List<DailyReportPojo> getFilteredReport(DailyReportForm dailyReportForm) throws ApiException {
        String startDateTime = dailyReportForm.getStartDate() + START_TIME;
        String endDateTime = dailyReportForm.getEndDate() + END_TIME;
        ZonedDateTime zonedStartDateTime;
        ZonedDateTime zonedEndDateTime;
        try{
            zonedStartDateTime = ZonedDateTime.parse(startDateTime);
            zonedEndDateTime = ZonedDateTime.parse(endDateTime);
        } catch (Exception e) {
            throw new ApiException("Invalid date format");
        }

        return dailyReportDao.selectFilteredReport(zonedStartDateTime, zonedEndDateTime);
    }

}

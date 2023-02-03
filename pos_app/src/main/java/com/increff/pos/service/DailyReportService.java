package com.increff.pos.service;

import com.increff.pos.dao.DailyReportDao;
import com.increff.pos.model.DailyReportForm;
import com.increff.pos.pojo.DailyReportPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional(rollbackFor = ApiException.class)
public class DailyReportService {

    private static final String SCHEDULER_TIME = "0 36 00 * * *";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String START_TIME = "t00:00:00.000Z";
    private static final String END_TIME = "t23:59:59.000Z";
    @Autowired
    private DailyReportDao dailyReportDao;
    @Autowired
    private OrderService orderService;

    public List<DailyReportPojo> getAll() throws ApiException {
        List<DailyReportPojo> dailyReportPojoList = dailyReportDao.selectAll(DailyReportPojo.class);
        Collections.sort(dailyReportPojoList, new Comparator<DailyReportPojo>() {
            @Override
            public int compare(DailyReportPojo dailyReportPojo1, DailyReportPojo dailyReportPojo2) {
                return dailyReportPojo2.getReportDate().compareTo(dailyReportPojo1.getReportDate());
            }
        });
        return dailyReportPojoList;
    }

    public List<DailyReportPojo> getFilteredReport(DailyReportForm dailyReportForm) throws ApiException {
        String startDateTime = dailyReportForm.getStartDate() + START_TIME;
        String endDateTime = dailyReportForm.getEndDate() + END_TIME;
        ZonedDateTime zonedStartDateTime = ZonedDateTime.parse(startDateTime);
        ZonedDateTime zonedEndDateTime = ZonedDateTime.parse(endDateTime);
        return dailyReportDao.selectFilteredReport(zonedStartDateTime, zonedEndDateTime);
    }

    @Scheduled(cron = SCHEDULER_TIME)
    public void dailyReportScheduled(){
        ZonedDateTime currentDateTime = ZonedDateTime.now().minusDays(1);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        String lastDayDate = currentDateTime.format(dateTimeFormatter);
        String startDate = lastDayDate + START_TIME;
        String endDate = lastDayDate + END_TIME;
        ZonedDateTime zonedStartDateTime = ZonedDateTime.parse(startDate);
        ZonedDateTime zonedEndDateTime = ZonedDateTime.parse(endDate);
        DailyReportPojo dailyReportPojo = generateDailyReport(zonedStartDateTime, zonedEndDateTime);
        dailyReportDao.insert(dailyReportPojo);
        System.out.println("Report Generated");
    }

    private DailyReportPojo generateDailyReport(ZonedDateTime startDateTime, ZonedDateTime endDateTime){
        List<OrderPojo> orderPojoList = orderService.getFilteredOrder(startDateTime, endDateTime);
        DailyReportPojo dailyReportPojo = new DailyReportPojo();
        dailyReportPojo.setReportDate(startDateTime);
        Integer totalOrder = 0;
        Integer totalItem = 0;
        Double totalRevenue = 0.0;
        for(OrderPojo orderPojo: orderPojoList){
            List<OrderItemPojo> orderItemPojoList = orderService.getOrderItemsByOrderId(orderPojo.getId());
            totalOrder++;
            for(OrderItemPojo orderItemPojo: orderItemPojoList){
                totalItem += orderItemPojo.getQuantity();
                totalRevenue += (orderItemPojo.getSellingPrice() * orderItemPojo.getQuantity());
            }
        }
        dailyReportPojo.setTotalOrders(totalOrder);
        dailyReportPojo.setTotalItems(totalItem);
        dailyReportPojo.setTotalRevenue(totalRevenue);
        return dailyReportPojo;
    }

}

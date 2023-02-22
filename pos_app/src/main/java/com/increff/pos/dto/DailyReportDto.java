package com.increff.pos.dto;

import com.increff.pos.model.datas.DailyReportData;
import com.increff.pos.model.forms.DailyReportForm;
import com.increff.pos.pojo.DailyReportPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.DailyReportService;
import com.increff.pos.service.OrderService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.increff.pos.util.Converter.convertDailyReportPojoToDailyReportData;
import static com.increff.pos.util.DateTimeFormat.*;
import static com.increff.pos.util.NormalizerUtil.normalize;
import static com.increff.pos.util.ValidatorUtil.validate;
import static com.increff.pos.util.ValidatorUtil.validateDates;

@Component
public class DailyReportDto {
    private static final String SCHEDULER_TIME = "0 55 10 * * *";
    private static final Logger logger = Logger.getLogger(DailyReportDto.class);
    @Autowired
    private DailyReportService dailyReportService;
    @Autowired
    private OrderService orderService;

    public List<DailyReportData> getAll() throws ApiException {
        return convertDailyReportPojoToDailyReportData(dailyReportService.getAll());
    }

    public List<DailyReportData> getFilteredReport(DailyReportForm dailyReportForm) throws ApiException, IllegalAccessException {
        validate(dailyReportForm);
        normalize(dailyReportForm);
        validateDates(dailyReportForm.getStartDate(), dailyReportForm.getEndDate());
        return convertDailyReportPojoToDailyReportData(dailyReportService.getFilteredReport(dailyReportForm));
    }

    @Scheduled(cron = SCHEDULER_TIME)
    public void dailyReportScheduled() {
        //currentDateTime gets the last day date for which daily report has to be generated
        ZonedDateTime currentDateTime = ZonedDateTime.now().minusDays(1);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_YMD);
        String lastDayDate = currentDateTime.format(dateTimeFormatter);
        String startDate = lastDayDate + START_TIME;
        String endDate = lastDayDate + END_TIME;
        ZonedDateTime zonedStartDateTime = ZonedDateTime.parse(startDate);
        ZonedDateTime zonedEndDateTime = ZonedDateTime.parse(endDate);
        generateDailyReport(zonedStartDateTime, zonedEndDateTime);

    }

    public void generateDailyReport(ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        //orderPojoList stores all the orders which were placed and invoice was created between startDateTime and endDateTime
        List<OrderPojo> orderPojoList = orderService.getPlacedOrdersFilteredByDate(startDateTime, endDateTime);
        DailyReportPojo dailyReportPojo = new DailyReportPojo();
        dailyReportPojo.setReportDate(startDateTime);
        Integer totalOrder = 0;
        Integer totalItem = 0;
        double totalRevenue = 0.0;
        for (OrderPojo orderPojo : orderPojoList) {
            List<OrderItemPojo> orderItemPojoList = orderService.getOrderItemsByOrderId(orderPojo.getId());
            totalOrder++;
            for (OrderItemPojo orderItemPojo : orderItemPojoList) {
                totalItem += orderItemPojo.getQuantity();
                totalRevenue += (orderItemPojo.getSellingPrice() * orderItemPojo.getQuantity());
            }
        }
        dailyReportPojo.setTotalOrders(totalOrder);
        dailyReportPojo.setTotalItems(totalItem);
        dailyReportPojo.setTotalRevenue(totalRevenue);
        dailyReportService.add(dailyReportPojo);
        logger.info("Daily Report generated successfully");
    }


}

package com.increff.pos.dto;

import com.increff.pos.model.datas.DailyReportData;
import com.increff.pos.model.forms.DailyReportForm;
import com.increff.pos.model.forms.OrderItemForm;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.increff.pos.util.DateTimeFormatForTest.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class DailyReportDtoTest extends AbstractUnitTest {
    @Autowired
    private OrderDto orderDto;

    @Autowired
    private OrderService orderService;
    @Autowired
    private DailyReportDto dailyReportDto;

    @Test
    public void testGetAllDailyReports() throws ApiException, IllegalAccessException {
        createDailyReport();
        DailyReportData dailyReportData = dailyReportDto.getAll().get(0);
        assertEquals((Integer) 3, dailyReportData.getTotalOrders());
        assertEquals((Integer) 1200, dailyReportData.getTotalItems());
        assertEquals((Double) 178200.0, dailyReportData.getTotalRevenue());
    }

    @Test
    public void testGetFilteredDailyReports() throws ApiException, IllegalAccessException {
        createDailyReport();
        ZonedDateTime currentDateTime = ZonedDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_YMD);
        String currentDayDate = currentDateTime.format(dateTimeFormatter);
        DailyReportForm dailyReportForm = new DailyReportForm();
        dailyReportForm.setStartDate(currentDayDate);
        dailyReportForm.setEndDate(currentDayDate);
        DailyReportData dailyReportData = dailyReportDto.getFilteredReport(dailyReportForm).get(0);

        assertEquals((Integer) 3, dailyReportData.getTotalOrders());
        assertEquals((Integer) 1200, dailyReportData.getTotalItems());
        assertEquals((Double) 178200.0, dailyReportData.getTotalRevenue());
    }
    @Test
    public void testDailyReportScheduled(){
        dailyReportDto.dailyReportScheduled();
    }


    private void createDailyReport() throws ApiException, IllegalAccessException {
        for (int i = 0; i < 3; i++) {
            List<OrderItemForm> orderItemFormList = createOrder(i);
            Integer orderId = orderDto.addOrder(orderItemFormList);
            orderService.placeOrder(orderId);
        }

        ZonedDateTime currentDateTime = ZonedDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_YMD);
        String currentDayDate = currentDateTime.format(dateTimeFormatter);
        String startDate = currentDayDate + START_TIME;
        String endDate = currentDayDate + END_TIME;
        ZonedDateTime zonedStartDateTime = ZonedDateTime.parse(startDate);
        ZonedDateTime zonedEndDateTime = ZonedDateTime.parse(endDate);

        dailyReportDto.generateDailyReport(zonedStartDateTime, zonedEndDateTime);
    }

}

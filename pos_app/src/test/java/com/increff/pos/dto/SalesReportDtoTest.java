package com.increff.pos.dto;

import com.increff.pos.model.datas.SalesReportData;
import com.increff.pos.model.forms.OrderItemForm;
import com.increff.pos.model.forms.SalesReportForm;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.increff.pos.util.DateTimeFormatForTest.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SalesReportDtoTest extends AbstractUnitTest {
    @Autowired
    private OrderDto orderDto;
    @Autowired
    private OrderService orderService;
    @Autowired
    private SalesReportDto salesReportDto;
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();


    @Test
    public void testGetAllSalesReports() throws ApiException, IllegalAccessException {
        createAndPlaceOrder();
        List<SalesReportData> salesReportDataList = salesReportDto.getAll();
        assertEquals(3, salesReportDataList.size());
        for (int i = 0; i < 3; i++) {
            matchData(i, salesReportDataList.get(i));
        }
    }

    @Test
    public void testGetFilteredSalesReports() throws ApiException, IllegalAccessException {
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
        startDate = startDate.substring(0, startDate.length() - 1);
        endDate = endDate.substring(0, endDate.length() - 1);
        SalesReportForm salesReportForm = new SalesReportForm();
        salesReportForm.setStartDate(startDate);
        salesReportForm.setEndDate(endDate);
        salesReportForm.setBrand("brand0");
        salesReportForm.setCategory("All");
        SalesReportData salesReportData = salesReportDto.getFilteredReport(salesReportForm).get(0);
        assertEquals("brand0", salesReportData.getBrand());
        assertEquals("category0", salesReportData.getCategory());
        assertEquals((Integer) 400, salesReportData.getQuantity());
        assertEquals((Double) 59400.0, salesReportData.getRevenue());
    }

//    @Test
//    public void testDownloadSalesReportCSV() throws ApiException, IllegalAccessException, IOException {
//        createAndPlaceOrder();
//        assertNotEquals(null, salesReportDto.downloadSalesReportCSV());
//    }


    private void matchData(Integer id, SalesReportData salesReportData) {
        assertEquals("brand" + id, salesReportData.getBrand());
        assertEquals("category" + id, salesReportData.getCategory());
        assertEquals((Integer) 400, salesReportData.getQuantity());
        assertEquals((Double) 59400.0, salesReportData.getRevenue());
    }

}

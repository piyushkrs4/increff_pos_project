package com.increff.pos.util;

import com.increff.pos.model.*;
import com.increff.pos.pojo.*;
import com.increff.pos.service.ApiException;
import org.springframework.beans.BeanUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Converter {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss z";
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final String UTC = "UTC";

    public static <T> T convertGeneric(Object input, Class<T> clazz) throws ApiException {
        T output = null;
        try {
            output = clazz.newInstance();
            BeanUtils.copyProperties(input, output);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ApiException("Error in conversion");
        }
        return output;
    }

    public static ProductData convertProductPojoToData(ProductPojo productPojo, BrandPojo brandPojo) throws ApiException {
        ProductData productData = new ProductData();
        productData.setBarcode(productPojo.getBarcode());
        productData.setBrand(brandPojo.getBrand());
        productData.setCategory(brandPojo.getCategory());
        productData.setName(productPojo.getName());
        productData.setMrp(productPojo.getMrp());
        productData.setId(productPojo.getId());
        return productData;
    }

    public static InventoryData convertInventoryPojoToData(InventoryPojo inventoryPojo, ProductPojo productPojo) {
        InventoryData inventoryData = new InventoryData();
        inventoryData.setBarcode(productPojo.getBarcode());
        inventoryData.setProductName(productPojo.getName());
        inventoryData.setQuantity(inventoryPojo.getQuantity());
        inventoryData.setId(inventoryPojo.getId());
        return inventoryData;
    }

    // todo -> make datetime string public static final
    public static OrderData convertOrderPojoToData(OrderPojo orderPojo) {
        OrderData orderData = new OrderData();
        ZonedDateTime dateTime = orderPojo.getCreatedAt().withZoneSameInstant(ZoneId.of(UTC));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
        String formattedDateTime = dateTime.format(dateTimeFormatter);
        orderData.setDatetime(formattedDateTime);
        orderData.setZonedDateTime(orderPojo.getCreatedAt());
        orderData.setId(orderPojo.getId());
        orderData.setStatus(orderPojo.getOrderStatus());
        orderData.setOrderCode(orderPojo.getOrderCode());
        return orderData;
    }

    public static List<DailyReportData> convertDailyReportPojoToDailyReportData(List<DailyReportPojo> dailyReportPojoList) throws ApiException {
        List<DailyReportData> dailyReportDataList = new ArrayList<>();
        for (DailyReportPojo dailyReportPojo : dailyReportPojoList) {
            DailyReportData dailyReportData = convertGeneric(dailyReportPojo, DailyReportData.class);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            String reportDate = dailyReportPojo.getReportDate().format(dateTimeFormatter);
            dailyReportData.setDate(reportDate);
            dailyReportDataList.add(dailyReportData);
        }
        return dailyReportDataList;
    }
}

package com.increff.pos.service;

import com.increff.pos.model.SalesReportData;
import com.increff.pos.model.SalesReportForm;
import com.increff.pos.pojo.*;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional(rollbackFor = ApiException.class)
public class SalesReportService {
    private static final String ALL = "all";
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private OrderService orderService;

    public List<SalesReportData> getAll() throws ApiException {
        List<OrderPojo> orderPojoList = orderService.getAllPlacedOrders();
        return generateSalesReport(orderPojoList, ALL, ALL);
    }

    public List<SalesReportData> getFilteredReport(SalesReportForm salesReportForm) throws ApiException {
        ZonedDateTime startDate = ZonedDateTime.parse(salesReportForm.getStartDate() + "Z");
        ZonedDateTime endDate = ZonedDateTime.parse(salesReportForm.getEndDate() + "Z");
        String brand = salesReportForm.getBrand();
        String category = salesReportForm.getCategory();
        List<OrderPojo> orderPojoList = orderService.getFilteredOrder(startDate, endDate);
        return generateSalesReport(orderPojoList, brand, category);
    }

    private List<SalesReportData> generateSalesReport(List<OrderPojo> orderPojoList, String brand, String category) throws ApiException {
        HashMap<Integer, SalesReportData> brandIdToSalesReportDataMap = new HashMap<>();
        for(OrderPojo orderPojo: orderPojoList){
            List<OrderItemPojo> orderItemPojoList = orderService.getOrderItemsByOrderId(orderPojo.getId());
            for(OrderItemPojo orderItemPojo: orderItemPojoList){
                ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
                if(!brandIdToSalesReportDataMap.containsKey(productPojo.getBrandId()))
                    brandIdToSalesReportDataMap.put(productPojo.getBrandId(), new SalesReportData());
                SalesReportData salesReportData = brandIdToSalesReportDataMap.get(productPojo.getBrandId());
                salesReportData.setQuantity(salesReportData.getQuantity() + orderItemPojo.getQuantity());
                salesReportData.setRevenue(salesReportData.getRevenue() + (orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice()));
            }
        }
        List<SalesReportData> salesReportDataList = new ArrayList<>();
        for(Map.Entry<Integer, SalesReportData> m : brandIdToSalesReportDataMap.entrySet()){
            BrandPojo brandPojo = brandService.get(m.getKey());
            if((Objects.equals(brandPojo.getBrand(), brand) || Objects.equals(ALL, brand)) && (Objects.equals(brandPojo.getCategory(), category) || Objects.equals(ALL, category))) {
                SalesReportData salesReportData = m.getValue();
                salesReportData.setBrand(brandPojo.getBrand());
                salesReportData.setCategory(brandPojo.getCategory());
                salesReportDataList.add(salesReportData);
            }
        }
        return salesReportDataList;
    }
}


package com.increff.pos.dto;

import com.increff.pos.model.datas.SalesReportData;
import com.increff.pos.model.forms.SalesReportForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.*;

import static com.increff.pos.util.NormalizerUtil.normalize;
import static com.increff.pos.util.ValidatorUtil.validate;
import static com.increff.pos.util.ValidatorUtil.validateDates;

@Component
public class SalesReportDto {
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

    public List<SalesReportData> getFilteredReport(SalesReportForm salesReportForm) throws ApiException, IllegalAccessException {
        validate(salesReportForm);
        normalize(salesReportForm);
        validateDates(salesReportForm.getStartDate(), salesReportForm.getEndDate());
        ZonedDateTime startDate;
        ZonedDateTime endDate;
        try {
            startDate = ZonedDateTime.parse(salesReportForm.getStartDate() + "Z");
            endDate = ZonedDateTime.parse(salesReportForm.getEndDate() + "Z");
        } catch (Exception e) {
            throw new ApiException("Invalid date format");
        }
        String brand = salesReportForm.getBrand();
        String category = salesReportForm.getCategory();
        List<OrderPojo> orderPojoList = orderService.getPlacedOrdersFilteredByDate(startDate, endDate);
        return generateSalesReport(orderPojoList, brand, category);
    }

    private List<SalesReportData> generateSalesReport(List<OrderPojo> orderPojoList, String brand, String category) throws ApiException {
        HashMap<Integer, SalesReportData> brandIdToSalesReportDataMap = new HashMap<>();
        for (OrderPojo orderPojo : orderPojoList) {
            List<OrderItemPojo> orderItemPojoList = orderService.getOrderItemsByOrderId(orderPojo.getId());
            for (OrderItemPojo orderItemPojo : orderItemPojoList) {
                ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
                if (!brandIdToSalesReportDataMap.containsKey(productPojo.getBrandId()))
                    brandIdToSalesReportDataMap.put(productPojo.getBrandId(), new SalesReportData());
                SalesReportData salesReportData = brandIdToSalesReportDataMap.get(productPojo.getBrandId());
                salesReportData.setQuantity(salesReportData.getQuantity() + orderItemPojo.getQuantity());
                salesReportData.setRevenue(salesReportData.getRevenue() + (orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice()));
            }
        }
        List<SalesReportData> salesReportDataList = new ArrayList<>();
        for (Map.Entry<Integer, SalesReportData> m : brandIdToSalesReportDataMap.entrySet()) {
            BrandPojo brandPojo = brandService.getBrand(m.getKey());
            if ((Objects.equals(brandPojo.getBrand(), brand) || Objects.equals(ALL, brand)) && (Objects.equals(brandPojo.getCategory(), category) || Objects.equals(ALL, category))) {
                SalesReportData salesReportData = m.getValue();
                salesReportData.setBrand(brandPojo.getBrand());
                salesReportData.setCategory(brandPojo.getCategory());
                salesReportDataList.add(salesReportData);
            }
        }
        return salesReportDataList;
    }

}

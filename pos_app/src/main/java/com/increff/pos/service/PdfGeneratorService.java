package com.increff.pos.service;


import com.increff.pos.model.InvoiceData;
import com.increff.pos.model.InvoiceLineItem;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.PdfGenerator;
import io.swagger.models.auth.In;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = ApiException.class)
public class PdfGeneratorService {
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final String TIME_FORMAT = "HH:mm:ss";
    @Autowired
    private PdfGenerator pdfGenerator;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    public void generatePdf(Integer orderId, String currentDateTimeData) throws ApiException {
        String currentDate = currentDateTimeData.substring(0, 8);
        String currentTime = currentDateTimeData.substring(11);
        OrderPojo orderPojo = orderService.getOrderByOrderId(orderId);
        InvoiceData invoiceData = getInvoiceDetails(orderPojo, currentDate, currentTime);
        orderService.placeOrder(orderId);
        pdfGenerator.xmlToPdfConverter(invoiceData);
    }

    public InvoiceData showPlacedOrder(Integer orderId) throws ApiException {
        OrderPojo orderPojo = orderService.getOrderByOrderId(orderId);
        if(!orderPojo.getOrderStatus())
            throw new ApiException("Order has not been placed");
        ZonedDateTime dateTime = orderPojo.getUpdatedAt();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        String formattedDate = dateTime.format(dateTimeFormatter);
        dateTimeFormatter = DateTimeFormatter.ofPattern(TIME_FORMAT);
        String formattedTime = dateTime.format(dateTimeFormatter);
        return getInvoiceDetails(orderPojo, formattedDate, formattedTime);
    }

    public InvoiceData getInvoiceDetails(OrderPojo orderPojo, String currentDate, String currentTime) throws ApiException {
        List<OrderItemPojo> orderItemPojoList = orderService.getOrderItemsByOrderId(orderPojo.getId());
        if(orderItemPojoList.isEmpty()){
            throw new ApiException("Select at least one item to order");
        }
        InvoiceData invoiceData = new InvoiceData();
        invoiceData.setInvoiceDate(currentDate);
        invoiceData.setInvoiceTime(currentTime);
        invoiceData.setInvoiceNumber(orderPojo.getId());
        invoiceData.setOrderCode(orderPojo.getOrderCode());
        Double totalAmount = 0.0;
        Integer sno = 1;
        List<InvoiceLineItem> invoiceLineItemList = new ArrayList<>();
        for(OrderItemPojo orderItemPojo: orderItemPojoList){
            ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
            InvoiceLineItem invoiceLineItem = new InvoiceLineItem();
            invoiceLineItem.setSno(sno++);
            invoiceLineItem.setProductName(productPojo.getName());
            invoiceLineItem.setBarcode(productPojo.getBarcode());
            invoiceLineItem.setQuantity(orderItemPojo.getQuantity());
            invoiceLineItem.setUnitPrice(orderItemPojo.getSellingPrice());
            invoiceLineItem.setTotal(orderItemPojo.getSellingPrice() * orderItemPojo.getQuantity());
            totalAmount += invoiceLineItem.getTotal();
            invoiceLineItemList.add(invoiceLineItem);
        }
        invoiceData.setTotal(totalAmount);
        invoiceData.setLineItems(invoiceLineItemList);
        return invoiceData;
    }

}

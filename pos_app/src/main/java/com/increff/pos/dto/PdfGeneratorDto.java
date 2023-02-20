package com.increff.pos.dto;

import com.increff.pos.model.datas.InvoiceData;
import com.increff.pos.model.datas.InvoiceLineItem;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static com.increff.pos.util.DateTimeFormat.DATE_FORMAT_DMY;
import static com.increff.pos.util.DateTimeFormat.TIME_FORMAT;

@Component
public class PdfGeneratorDto {

    private static final String PDF_FILE_PATH = "src/main/resources/PdfFiles/invoice_";
    private final Logger logger = Logger.getLogger(PdfGeneratorDto.class);
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private RestTemplate restTemplate;

    @Value("${pdfApp.url}")
    private String pdfAppUrl;

    @Value("${pdfFilePath}")
    private String pdfFilePath;
    public void generatePdf(Integer orderId) throws ApiException {
        ZonedDateTime currentZonedDateTime = ZonedDateTime.now();
        OrderPojo orderPojo = orderService.getOrderPojoByOrderId(orderId);
        if(orderPojo.getOrderStatus())
            throw new ApiException("Order is already placed. Please reload the page!");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_DMY);
        String currentDate = currentZonedDateTime.format(dateTimeFormatter);
        dateTimeFormatter = DateTimeFormatter.ofPattern(TIME_FORMAT);
        String currentTime = currentZonedDateTime.format(dateTimeFormatter);
        InvoiceData invoiceData = getInvoiceDetails(orderPojo, currentDate, currentTime);
        String base64;
        try{
            base64 = restTemplate.postForEntity(pdfAppUrl, invoiceData, String.class).getBody();
        } catch (Exception e){
            throw new ApiException("Unable to create invoice as invoice-app is not running.");
        }
        File pdfDir = new File(pdfFilePath);
        if(!pdfDir.mkdirs())
            logger.info("PdfFiles folder created successfully");
        String pdfFileName = "invoice_" + invoiceData.getInvoiceNumber() + ".pdf";
        File file = new File(pdfDir, pdfFileName);

        try (FileOutputStream fos = new FileOutputStream(file) ) {
            byte[] decoder = Base64.getDecoder().decode(base64);
            fos.write(decoder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        orderService.placeOrder(orderId);
    }

    public InvoiceData showPlacedOrder(Integer orderId) throws ApiException {
        OrderPojo orderPojo = orderService.getOrderPojoByOrderId(orderId);
        if(!orderPojo.getOrderStatus())
            throw new ApiException("Order has not been placed");
        ZonedDateTime dateTime = orderPojo.getUpdatedAt();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_DMY);
        String formattedDate = dateTime.format(dateTimeFormatter);
        dateTimeFormatter = DateTimeFormatter.ofPattern(TIME_FORMAT);
        String formattedTime = dateTime.format(dateTimeFormatter);
        return getInvoiceDetails(orderPojo, formattedDate, formattedTime);
    }

    public String downloadInvoice(Integer orderId) throws IOException {
        String filePath = PDF_FILE_PATH + orderId + ".pdf";

        File file = new File(filePath);
        byte[] bytesArray = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(bytesArray);
        fis.close();
        return Base64.getEncoder().encodeToString(bytesArray);
    }

    private InvoiceData getInvoiceDetails(OrderPojo orderPojo, String currentDate, String currentTime) throws ApiException {
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
        int sno = 1;
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

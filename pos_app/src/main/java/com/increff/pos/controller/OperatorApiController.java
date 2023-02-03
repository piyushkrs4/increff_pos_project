package com.increff.pos.controller;

import com.increff.pos.dto.*;
import com.increff.pos.model.*;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

@Api
@RestController
@RequestMapping(path = "/api/operator")
public class OperatorApiController {

    private static final String PDF_FILE_PATH = "D:\\WorkSpace\\IncreffProjects\\increff_pos_project\\src\\main\\resources\\com.increff.pdf\\PdfFiles\\invoice_";
    @Autowired
    private BrandDto brandDto;
    @Autowired
    private ProductDto productDto;
    @Autowired
    private InventoryDto inventoryDto;
    @Autowired
    private OrderDto orderDto;
    @Autowired
    private PdfGeneratorDto pdfGeneratorDto;

    @ApiOperation(value = "Gets list of all brands")
    @RequestMapping(path = "/brands", method = RequestMethod.GET)
    public List<BrandData> getAllBrands() throws ApiException {
        return brandDto.getAll();
    }

    @ApiOperation(value = "Gets list of all products")
    @RequestMapping(path = "/products", method = RequestMethod.GET)
    public List<ProductData> getAllProducts() throws ApiException {
        return productDto.getAll();
    }

    @ApiOperation(value = "Gets list of all inventories")
    @RequestMapping(path = "/inventories", method = RequestMethod.GET)
    public List<InventoryData> getAllInventories() throws ApiException {
        return inventoryDto.getAll();
    }

    @ApiOperation(value = "Adds all OrderItems and creates new order")
    @RequestMapping(path = "/orders", method = RequestMethod.POST)
    public void addOrder(@RequestBody List<OrderItemForm> orderItemFormList) throws ApiException, IllegalAccessException {
        orderDto.addOrder(orderItemFormList);
    }

    @ApiOperation(value = "Updates all order item")
    @RequestMapping(path = "orders/{orderId}", method = RequestMethod.PUT)
    public void updateOrder(@PathVariable Integer orderId, @RequestBody List<OrderItemForm> orderItemFormList) throws ApiException, IllegalAccessException {
        orderDto.updateOrder(orderId, orderItemFormList);
    }

    @ApiOperation(value = "Gets list of all orders")
    @RequestMapping(path = "/orders", method = RequestMethod.GET)
    public List<OrderData> getAllOrders() {
        return orderDto.getAllOrders();
    }

    @ApiOperation(value = "Gets list of all order-items of an order")
    @RequestMapping(path = "/orders/{orderId}/order-items", method = RequestMethod.GET)
    public List<OrderItemData> getOrderItemsByOrderId(@PathVariable Integer orderId) throws ApiException {
        return orderDto.getOrderItemsByOrderId(orderId);
    }

    @ApiOperation(value = "Generate Pdf")
    @RequestMapping(path = "/orders/{orderId}/generate-invoice", method = RequestMethod.POST)
    public void generateInvoice(@PathVariable Integer orderId, @RequestBody String currentDateTimeData) throws ApiException {
        pdfGeneratorDto.generatePdf(orderId, currentDateTimeData);
    }

    @ApiOperation(value = "Show Placed Order")
    @RequestMapping(path = "/orders/{orderId}/view-invoice", method = RequestMethod.GET)
    public InvoiceData showPlacedOrder(@PathVariable Integer orderId) throws ApiException {
        return pdfGeneratorDto.showPlacedOrder(orderId);
    }

    @ApiOperation(value = "Download Pdf")
    @RequestMapping(value="/orders/{orderId}/download-invoice", method=RequestMethod.GET)
    public String getInvoice(@PathVariable Integer orderId) throws IOException {
        String filePath = PDF_FILE_PATH + orderId + ".pdf";
        byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));
        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        return encodedString;
    }
}

package com.increff.pos.controller;

import com.increff.pos.dto.*;
import com.increff.pos.model.*;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Api
@RestController
@RequestMapping(path = "/api/operator")
public class OperatorApiController {
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
    public String downloadInvoice(@PathVariable Integer orderId) throws IOException {
        return pdfGeneratorDto.downloadInvoice(orderId);
    }
}

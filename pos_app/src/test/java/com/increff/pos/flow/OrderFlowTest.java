package com.increff.pos.flow;

import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.service.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderFlowTest extends AbstractUnitTest {
    @Autowired
    private OrderFlow orderFlow;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;

    @Test
    public void testAddOrder() throws ApiException {
        List<OrderItemPojo> orderItemPojoList = createOrderItemPojoList(0);
        List<String> barcodeList = new ArrayList<>();
        barcodeList.add("barcode0");
        barcodeList.add("0barcode");
        Integer orderId = orderFlow.addOrder(orderItemPojoList, barcodeList);
        matchOrder(0, orderService.getOrderItemsByOrderId(orderId));
    }

    @Test(expected = ApiException.class)
    public void testAddEmptyOrder() throws ApiException {
        orderFlow.addOrder(new ArrayList<>(), new ArrayList<>());
    }

    @Test
    public void testUpdateOrder() throws ApiException {
        List<OrderItemPojo> orderItemPojoList = createOrderItemPojoList(0);
        List<String> barcodeList = new ArrayList<>();
        barcodeList.add("barcode0");
        barcodeList.add("0barcode");
        Integer orderId = orderFlow.addOrder(orderItemPojoList, barcodeList);
        List<OrderItemPojo> newOrderItemPojoList = createOrderItemPojoList(1);
        for (OrderItemPojo orderItemPojo : newOrderItemPojoList)
            orderItemPojo.setOrderId(orderId);
        List<String> newBarcodeList = new ArrayList<>();
        newBarcodeList.add("barcode1");
        newBarcodeList.add("1barcode");
        orderFlow.updateOrder(orderId, newOrderItemPojoList, newBarcodeList);
        matchOrder(1, orderService.getOrderItemsByOrderId(orderId));
        assertEquals((Integer) 400, inventoryService.getUsingProductId(productService.getIdFromBarcode("barcode0")).getQuantity());
        assertEquals((Integer) 400, inventoryService.getUsingProductId(productService.getIdFromBarcode("0barcode")).getQuantity());
    }

    private void matchOrder(Integer id, List<OrderItemPojo> orderItemPojoList) throws ApiException {
        Integer productId1 = productService.getIdFromBarcode("barcode" + id);
        assertEquals(productId1, orderItemPojoList.get(0).getProductId());
        assertEquals((Integer) 10, orderItemPojoList.get(0).getQuantity());
        assertEquals((Double) 20.0, orderItemPojoList.get(0).getSellingPrice());
        assertEquals((Integer) 390, inventoryService.getUsingProductId(productId1).getQuantity());

        Integer productId2 = productService.getIdFromBarcode(id + "barcode");
        assertEquals(productId2, orderItemPojoList.get(1).getProductId());
        assertEquals((Integer) 10, orderItemPojoList.get(1).getQuantity());
        assertEquals((Double) 20.0, orderItemPojoList.get(1).getSellingPrice());
        assertEquals((Integer) 390, inventoryService.getUsingProductId(productId2).getQuantity());
    }

}

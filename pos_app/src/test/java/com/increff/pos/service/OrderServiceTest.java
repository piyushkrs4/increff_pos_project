package com.increff.pos.service;

import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderServiceTest extends AbstractUnitTest{

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Test
    public void testCreateOrder() throws ApiException {
        List<OrderItemPojo> orderItemPojoList = createOrderItemPojoList(0);
        Integer orderId = orderService.createOrder(orderItemPojoList);
        matchOrder(0, orderService.getOrderItemsByOrderId(orderId));
    }

    @Test
    public void testUpdateOrder() throws ApiException {
        List<OrderItemPojo> orderItemPojoList = createOrderItemPojoList(0);
        Integer orderId = orderService.createOrder(orderItemPojoList);
        for(OrderItemPojo orderItemPojo: orderItemPojoList)
            orderService.deleteOrderItem(orderItemPojo.getId());
        List<OrderItemPojo> newOrderItemPojoList = createOrderItemPojoList(1);
        for(OrderItemPojo orderItemPojo: newOrderItemPojoList){
            orderItemPojo.setOrderId(orderId);
        }
        orderService.updateOrder(newOrderItemPojoList);
        matchOrder(1, orderService.getOrderItemsByOrderId(orderId));
    }

    @Test
    public void testGetAllOrders() throws ApiException {
        List<OrderItemPojo> orderItemPojoList = createOrderItemPojoList(0);
        Integer orderId = orderService.createOrder(orderItemPojoList);
        assertEquals(orderId, orderService.getAllOrders().get(0).getId());
    }

    @Test
    public void testGetOrderPojoByOrderId() throws ApiException {
        List<OrderItemPojo> orderItemPojoList = createOrderItemPojoList(0);
        Integer orderId = orderService.createOrder(orderItemPojoList);
        OrderPojo orderPojo = orderService.getOrderPojoByOrderId(orderId);
        assertEquals("order_" + orderId, orderPojo.getOrderCode());
    }

    @Test
    public void testGetAllPlacedOrders() throws ApiException {
        List<OrderItemPojo> orderItemPojoList = createOrderItemPojoList(0);
        Integer orderId = orderService.createOrder(orderItemPojoList);
        orderService.placeOrder(orderId);
        OrderPojo orderPojo = orderService.getAllPlacedOrders().get(0);
        assertEquals("order_" + orderId, orderPojo.getOrderCode());
    }

    @Test
    public void TestGetOrdersFilteredByDate() throws ApiException {
        List<OrderItemPojo> orderItemPojoList = createOrderItemPojoList(0);
        Integer orderId = orderService.createOrder(orderItemPojoList);
        orderService.placeOrder(orderId);
        ZonedDateTime startDate = ZonedDateTime.now().minusDays(1);
        ZonedDateTime endDate = ZonedDateTime.now().plusDays(1);
        OrderPojo orderPojo = orderService.getPlacedOrdersFilteredByDate(startDate, endDate).get(0);
        assertEquals("order_" + orderId, orderPojo.getOrderCode());
    }

    @Test(expected = ApiException.class)
    public void testCreateOrderWithEmptyOrderId() throws ApiException {
        List<OrderItemPojo> orderItemPojoList = createOrderItemPojoList(0);
        orderItemPojoList.get(0).setSellingPrice(null);
        orderService.createOrder(orderItemPojoList);
    }

    @Test(expected = ApiException.class)
    public void testGetOrderPojoWithIncorrectId() throws ApiException {
        orderService.getOrderPojoByOrderId(1);
    }

    @Test(expected = ApiException.class)
    public void testDeleteOrderItemWithIncorrectId() throws ApiException {
        orderService.deleteOrderItem(1);
    }


    private void matchOrder(Integer id, List<OrderItemPojo> orderItemPojoList) throws ApiException {
        Integer productId1 = productService.getIdFromBarcode("barcode" + id);
        assertEquals(productId1, orderItemPojoList.get(0).getProductId());
        assertEquals((Integer) 10, orderItemPojoList.get(0).getQuantity());
        assertEquals((Double) 20.0, orderItemPojoList.get(0).getSellingPrice());

        Integer productId2 = productService.getIdFromBarcode(id + "barcode");
        assertEquals(productId2, orderItemPojoList.get(1).getProductId());
        assertEquals((Integer) 10, orderItemPojoList.get(1).getQuantity());
        assertEquals((Double) 20.0, orderItemPojoList.get(1).getSellingPrice());
    }
}

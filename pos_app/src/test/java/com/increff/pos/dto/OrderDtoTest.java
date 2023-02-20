package com.increff.pos.dto;

import com.increff.pos.model.datas.InventoryData;
import com.increff.pos.model.datas.OrderData;
import com.increff.pos.model.datas.OrderItemData;
import com.increff.pos.model.forms.OrderItemForm;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertEquals;

public class OrderDtoTest extends AbstractUnitTest {
    @Autowired
    private InventoryDto inventoryDto;
    @Autowired
    private OrderDto orderDto;
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();


    @Test
    public void testAddOrder() throws ApiException, IllegalAccessException {
        List<OrderItemForm> orderItemFormList = createOrder(0);
        orderDto.addOrder(orderItemFormList);

        OrderData orderData = orderDto.getAllOrders().get(0);
        List<OrderItemData> orderItemDataList = orderDto.getOrderItemsByOrderId(orderData.getId());

        assertEquals(false, orderData.getStatus());
        matchData(0, orderItemDataList);
    }


//    @Test
//    public void testInsufficientInventoryOnAddOrder() throws ApiException, IllegalAccessException {
//        List<OrderItemForm> orderItemFormList = createOrder(0);
//        orderItemFormList.get(0).setQuantity(10);
//
//        exceptionRule.expect(ApiException.class);
//        exceptionRule.expectMessage(startsWith("Cannot order more than"));
//        orderDto.addOrder(orderItemFormList);
//    }

    @Test
    public void testSellingPriceGreaterThanMrpOnAddOrder() throws ApiException, IllegalAccessException {
        List<OrderItemForm> orderItemFormList = createOrder(0);
        orderItemFormList.get(0).setSellingPrice(1000.0);

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Selling Price cannot be greater than MRP Rs.100.0 for barcode: barcode0");
        orderDto.addOrder(orderItemFormList);
    }

    @Test
    public void testInvalidBarcodeOnAddOrder() throws ApiException, IllegalAccessException {
        List<OrderItemForm> orderItemFormList = createOrder(0);
        orderItemFormList.get(0).setBarcode("barcode9");

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Product with barcode: barcode9 does not exit!");
        orderDto.addOrder(orderItemFormList);
    }

    @Test(expected = ApiException.class)
    public void testEmptyBarcodeOnAddOrder() throws ApiException, IllegalAccessException {
        List<OrderItemForm> orderItemFormList = createOrder(0);
        orderItemFormList.get(0).setBarcode("");
        orderDto.addOrder(orderItemFormList);
    }

    @Test(expected = ApiException.class)
    public void testEmptyQuantityOnAddOrder() throws ApiException, IllegalAccessException {
        List<OrderItemForm> orderItemFormList = createOrder(0);
        orderItemFormList.get(0).setQuantity(null);
        orderDto.addOrder(orderItemFormList);
    }


    @Test(expected = ApiException.class)
    public void testEmptySellingPriceOnAddOrder() throws ApiException, IllegalAccessException {
        List<OrderItemForm> orderItemFormList = createOrder(0);
        orderItemFormList.get(0).setSellingPrice(null);
        orderDto.addOrder(orderItemFormList);
    }

    @Test
    public void testGetAllOrders() throws ApiException, IllegalAccessException {
        for (int i = 0; i < 5; i++) {
            List<OrderItemForm> orderItemFormList = createOrder(i);
            orderDto.addOrder(orderItemFormList);
        }
        List<OrderData> orderDataList = orderDto.getAllOrders();
        assertEquals(5, orderDataList.size());
    }

    @Test
    public void testGetOrderItemsOrderId() throws ApiException, IllegalAccessException {
        List<OrderItemForm> orderItemFormList = createOrder(0);
        Integer orderId = orderDto.addOrder(orderItemFormList);
        List<OrderItemData> orderItemDataList = orderDto.getOrderItemsByOrderId(orderId);

        matchData(0, orderItemDataList);

    }

    @Test
    public void testUpdateOrder() throws ApiException, IllegalAccessException {
        List<OrderItemForm> orderItemFormList = createOrder(0);
        Integer orderId = orderDto.addOrder(orderItemFormList);

        List<OrderItemForm> newOrderItemFormList = createOrder(1);
        orderDto.updateOrder(orderId, newOrderItemFormList);

        List<OrderItemData> orderItemDataList = orderDto.getOrderItemsByOrderId(orderId);
        matchData(1, orderItemDataList);
        List<InventoryData> inventoryDataList = inventoryDto.getAll();
        assertEquals((Integer) 400, inventoryDataList.get(0).getQuantity());
        assertEquals((Integer) 400, inventoryDataList.get(1).getQuantity());
    }

    @Test(expected = ApiException.class)
    public void testNegativeQuantityOnUpdateOrder() throws ApiException, IllegalAccessException {
        List<OrderItemForm> orderItemFormList = createOrder(0);
        Integer orderId = orderDto.addOrder(orderItemFormList);

        List<OrderItemForm> newOrderItemFormList = createOrder(1);
        newOrderItemFormList.get(0).setQuantity(-1);
        orderDto.updateOrder(orderId, newOrderItemFormList);
    }

    @Test(expected = ApiException.class)
    public void testSellingPriceGreaterThanMrpOnUpdateOrder() throws ApiException, IllegalAccessException {
        List<OrderItemForm> orderItemFormList = createOrder(0);
        Integer orderId = orderDto.addOrder(orderItemFormList);

        List<OrderItemForm> newOrderItemFormList = createOrder(1);
        newOrderItemFormList.get(0).setSellingPrice(1000.0);
        orderDto.updateOrder(orderId, newOrderItemFormList);
    }


    private void matchData(Integer id, List<OrderItemData> orderItemDataList) throws ApiException {
        assertEquals(2, orderItemDataList.size());

        OrderItemData orderItemData = orderItemDataList.get(0);
        assertEquals("product" + id, orderItemData.getProductName());
        assertEquals("barcode" + id, orderItemData.getBarcode());
        assertEquals((Integer) 200, orderItemData.getQuantity());
        assertEquals((Double) 98.5, orderItemData.getSellingPrice());

        orderItemData = orderItemDataList.get(1);
        assertEquals(id + "product", orderItemData.getProductName());
        assertEquals(id + "barcode", orderItemData.getBarcode());
        assertEquals((Integer) 200, orderItemData.getQuantity());
        assertEquals((Double) 198.5, orderItemData.getSellingPrice());

        List<InventoryData> inventoryDataList = inventoryDto.getAll();
        assertEquals((Integer) 200, inventoryDataList.get(id * 2).getQuantity());
        assertEquals((Integer) 200, inventoryDataList.get(id * 2 + 1).getQuantity());

    }
}

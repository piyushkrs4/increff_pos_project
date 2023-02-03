package com.increff.pos.service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional(rollbackFor = ApiException.class)
public class OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;

    public void addOrder(List<OrderItemPojo> orderItemPojoList) throws ApiException {
        OrderPojo orderPojo = new OrderPojo();
        orderDao.insert(orderPojo);
        String orderCode = "order_" + orderPojo.getId();
        orderPojo.setOrderCode(orderCode);
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            orderItemPojo.setOrderId(orderPojo.getId());
            addOrderItem(orderItemPojo);
        }
    }

    public void updateOrder(Integer orderId, List<OrderItemPojo> orderItemPojoList) throws ApiException {
        List<OrderItemPojo> exorderItemPojoList = getOrderItemsByOrderId(orderId);
        for (OrderItemPojo exorderItemPojo : exorderItemPojoList) {
            deleteOrderItem(exorderItemPojo.getId());
        }
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            addOrderItem(orderItemPojo);
        }
    }

    public Boolean getOrderStatus(Integer orderId) throws ApiException {
        OrderPojo orderPojo = getOrderByOrderId(orderId);
        return orderPojo.getOrderStatus();
    }

    public List<OrderPojo> getAllOrders() {
        List<OrderPojo> orderPojoList = orderDao.selectAll(OrderPojo.class);
        return  orderPojoList;
    }

    public OrderPojo getOrderByOrderId(Integer orderId) throws ApiException {
        return getOrderCheck(orderId);
    }

    public List<OrderPojo> getAllPlacedOrders() {
        return orderDao.selectAllPlaced();
    }

    public List<OrderPojo> getFilteredOrder(ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        return  orderDao.selectFilteredOrder(startDateTime, endDateTime);
    }

    public void placeOrder(Integer orderId) throws ApiException {
        OrderPojo orderPojo = getOrderCheck(orderId);
        orderPojo.setOrderStatus(true);
        orderDao.update(orderPojo);
    }

    public List<OrderItemPojo> getOrderItemsByOrderId(Integer orderId) {
        return orderItemDao.selectByOrderId(orderId);
    }

    private OrderPojo getOrderCheck(Integer orderId) throws ApiException {
        OrderPojo orderPojo = orderDao.select(orderId, OrderPojo.class);
        if (orderPojo == null) {
            throw new ApiException("Order with given ID does not exit, id: " + orderId);
        }
        return orderPojo;
    }

    private OrderItemPojo getOrderItemCheck(Integer orderId) throws ApiException {
        OrderItemPojo p = orderItemDao.select(orderId, OrderItemPojo.class);
        if (p == null) {
            throw new ApiException("Order Item with given ID does not exit, id: " + orderId);
        }
        return p;
    }

    private void addOrderItem(OrderItemPojo orderItemPojo) throws ApiException {
        getOrderItemCheckedAndUpdate(orderItemPojo, 0);
        OrderItemPojo exOrderItemPojo = orderItemDao.selectUsingProductIdAndOrderId(orderItemPojo.getProductId(), orderItemPojo.getOrderId());
        if (exOrderItemPojo == null)
            orderItemDao.insert(orderItemPojo);
        else {
            exOrderItemPojo.setQuantity(exOrderItemPojo.getQuantity() + orderItemPojo.getQuantity());
            orderItemDao.update(exOrderItemPojo);
        }
    }

    private void deleteOrderItem(Integer orderItemId) throws ApiException {
        OrderItemPojo orderItemPojo = getOrderItemCheck(orderItemId);
        InventoryPojo inventoryPojo = inventoryService.getUsingProductId(orderItemPojo.getProductId());
        inventoryPojo.setQuantity(inventoryPojo.getQuantity() + orderItemPojo.getQuantity());
        inventoryService.update(inventoryPojo.getId(), inventoryPojo);
        orderItemDao.delete(orderItemId, OrderItemPojo.class);
    }

    private void getOrderItemCheckedAndUpdate(OrderItemPojo orderItemPojo, Integer lastQuantity) throws ApiException {
        if (orderItemPojo.getQuantity() < 0)
            throw new ApiException("Product quantity cannot be less than zero");
        InventoryPojo ip = inventoryService.getUsingProductId(orderItemPojo.getProductId());
        if (ip == null)
            throw new ApiException("Item is not available in inventory");
        if (ip.getQuantity() + lastQuantity < orderItemPojo.getQuantity())
            throw new ApiException("Available Product quantity is less than required quantity. Available quantity: " + ip.getQuantity());
        ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
        if (productPojo.getMrp() < orderItemPojo.getSellingPrice())
            throw new ApiException("Selling Price cannot be greater than MRP. MRP is Rs. " + productPojo.getMrp());
        ip.setQuantity(ip.getQuantity() + lastQuantity - orderItemPojo.getQuantity());
        inventoryService.update(ip.getId(), ip);
    }

}

// todo -> update inventory using single method

// todo-> ctrl + alt + L -> refactor code
// todo-> ctrl + alt + o -> remove unused dependencies

// todo-> use java functions whenever and wherever available

//todo -> https://www.baeldung.com/java-bean-validation-not-null-empty-blank


//    public void update(Integer orderItemId, OrderItemPojo orderItemPojo) throws ApiException {
//        OrderItemPojo exOrderItemPojo = getCheck(orderItemId);
//        orderItemPojo.setProductId(exOrderItemPojo.getProductId());
//        getCheckedAndUpdate(orderItemPojo, exOrderItemPojo.getQuantity());
//        exOrderItemPojo.setQuantity(orderItemPojo.getQuantity());
//        exOrderItemPojo.setSellingPrice(orderItemPojo.getSellingPrice());
//        orderItemDao.update(exOrderItemPojo);
//    }
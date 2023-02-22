package com.increff.pos.service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackFor = Exception.class)
public class OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemDao orderItemDao;

    public Integer createOrder(List<OrderItemPojo> orderItemPojoList) throws ApiException {
        OrderPojo orderPojo = new OrderPojo();
        orderDao.insert(orderPojo);
        String orderCode = "order_" + orderPojo.getId();
        orderPojo.setOrderCode(orderCode);
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            try{
                orderItemPojo.setOrderId(orderPojo.getId());
                orderItemDao.insert(orderItemPojo);
            }
            catch (Exception exception){
                throw new ApiException("Unable to add order items!");
            }
        }
        return orderPojo.getId();
    }

    public void updateOrder(List<OrderItemPojo> orderItemPojoList) throws ApiException {
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            try{
                orderItemDao.insert(orderItemPojo);
            }
            catch (Exception exception){
                throw new ApiException("Unable to update order!");
            }

        }
    }

    public List<OrderPojo> getAllOrders() {
        List<OrderPojo> orderPojoList = orderDao.selectAll(OrderPojo.class);
        orderPojoList.sort((orderPojo1, orderPojo2) -> orderPojo2.getCreatedAt().compareTo(orderPojo1.getCreatedAt()));
        return orderPojoList;
    }

    public OrderPojo getOrderPojoByOrderId(Integer orderId) throws ApiException {
        return getOrderCheck(orderId);
    }

    public List<OrderPojo> getAllPlacedOrders() {
        return orderDao.selectAllPlaced();
    }

    public List<OrderPojo> getPlacedOrdersFilteredByDate(ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        return orderDao.selectFilteredOrder(startDateTime, endDateTime);
    }

    public List<OrderItemPojo> getOrderItemsByOrderId(Integer orderId) {
        return orderItemDao.selectByOrderId(orderId);
    }

    public void placeOrder(Integer orderId) throws ApiException {
        OrderPojo orderPojo = getOrderCheck(orderId);
        orderPojo.setOrderStatus(true);
    }

    public void deleteOrderItem(Integer orderItemId) throws ApiException {
        getOrderItemCheck(orderItemId);
        orderItemDao.delete(orderItemId, OrderItemPojo.class);
    }

    private OrderPojo getOrderCheck(Integer orderId) throws ApiException {
        OrderPojo orderPojo = orderDao.select(orderId, OrderPojo.class);
        if (Objects.isNull(orderPojo)) {
            throw new ApiException("Order with order ID " + orderId + " does not exit!");
        }
        return orderPojo;
    }

    private void getOrderItemCheck(Integer orderId) throws ApiException {
        OrderItemPojo orderItemPojo = orderItemDao.select(orderId, OrderItemPojo.class);
        if (Objects.isNull(orderItemPojo)) {
            throw new ApiException("Order items with order ID " + orderId + " does not exit!");
        }
    }

}
//package com.increff.pos.service;
//
//import com.increff.pos.dao.OrderItemDao;
//import com.increff.pos.pojo.InventoryPojo;
//import com.increff.pos.pojo.OrderItemPojo;
//import com.increff.pos.pojo.ProductPojo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//@Transactional(rollbackFor = ApiException.class)
//public class OrderItemService {
//    @Autowired
//    private OrderItemDao orderItemDao;
//    @Autowired
//    private ProductService productService;
//    @Autowired
//    private InventoryService inventoryService;
//
//    public void add(OrderItemPojo orderItemPojo) throws ApiException {
//        getCheckedAndUpdate(orderItemPojo, 0);
//        OrderItemPojo exOrderItemPojo = orderItemDao.selectUsingProductIdAndOrderId(orderItemPojo.getProductId(), orderItemPojo.getOrderId());
//        if (exOrderItemPojo == null)
//            orderItemDao.insert(orderItemPojo);
//        else {
//            exOrderItemPojo.setQuantity(exOrderItemPojo.getQuantity() + orderItemPojo.getQuantity());
//            orderItemDao.update(exOrderItemPojo);
//        }
//    }
//
//    public void delete(Integer orderItemId) throws ApiException {
//        OrderItemPojo orderItemPojo = getCheck(orderItemId);
//        InventoryPojo inventoryPojo = inventoryService.getUsingProductId(orderItemPojo.getProductId());
//        inventoryPojo.setQuantity(inventoryPojo.getQuantity() + orderItemPojo.getQuantity());
//        inventoryService.update(inventoryPojo.getId(), inventoryPojo);
//        orderItemDao.delete(orderItemId, OrderItemPojo.class);
//    }
//
//    public List<OrderItemPojo> getByOrderId(Integer orderId) {
//        return orderItemDao.selectByOrderId(orderId);
//    }
//
////    public void update(Integer orderItemId, OrderItemPojo orderItemPojo) throws ApiException {
////        OrderItemPojo exOrderItemPojo = getCheck(orderItemId);
////        orderItemPojo.setProductId(exOrderItemPojo.getProductId());
////        getCheckedAndUpdate(orderItemPojo, exOrderItemPojo.getQuantity());
////        exOrderItemPojo.setQuantity(orderItemPojo.getQuantity());
////        exOrderItemPojo.setSellingPrice(orderItemPojo.getSellingPrice());
////        orderItemDao.update(exOrderItemPojo);
////    }
//
//    private OrderItemPojo getCheck(Integer orderId) throws ApiException {
//        OrderItemPojo p = orderItemDao.select(orderId, OrderItemPojo.class);
//        if (p == null) {
//            throw new ApiException("Order Item with given ID does not exit, id: " + orderId);
//        }
//        return p;
//    }
//
//    private void getCheckedAndUpdate(OrderItemPojo orderItemPojo, Integer lastQuantity) throws ApiException {
//        if (orderItemPojo.getQuantity() < 0)
//            throw new ApiException("Product quantity cannot be less than zero");
//        InventoryPojo ip = inventoryService.getUsingProductId(orderItemPojo.getProductId());
//        if (ip == null)
//            throw new ApiException("Item is not available in inventory");
//        if (ip.getQuantity() + lastQuantity < orderItemPojo.getQuantity())
//            throw new ApiException("Available Product quantity is less than required quantity. Available quantity: " + ip.getQuantity());
//        ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
//        if (productPojo.getMrp() < orderItemPojo.getSellingPrice())
//            throw new ApiException("Selling Price cannot be greater than MRP. MRP is Rs. " + productPojo.getMrp());
//        ip.setQuantity(ip.getQuantity() + lastQuantity - orderItemPojo.getQuantity());
//        inventoryService.update(ip.getId(), ip);
//    }
//
//}

package com.increff.pos.flow;

import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.increff.pos.util.GenericFunctionsUtil.addToErrorList;
import static com.increff.pos.util.GenericFunctionsUtil.throwApiErrorsList;

@Service
@Transactional(rollbackFor = Exception.class)
public class OrderFlow {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;

    public Integer addOrder(List<OrderItemPojo> orderItemPojoList, List<String> barcodeList) throws ApiException {
        if (orderItemPojoList.isEmpty())
            throw new ApiException("Add at least one item to order!");
        Set<String> set = new HashSet<>(barcodeList);
        if (set.size() < barcodeList.size()) {
            throw new ApiException("There are some duplicate order items!");
        }
        StringBuilder errors = new StringBuilder();
        int i = 0;
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            try {
                orderItemPojo.setProductId(productService.getIdFromBarcode(barcodeList.get(i++)));
                ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
                if (productPojo.getMrp() < orderItemPojo.getSellingPrice())
                    throw new ApiException("Selling Price cannot be greater than MRP Rs." + productPojo.getMrp() + " for barcode: " + productPojo.getBarcode());
                inventoryService.updateInventoryOnOrder(orderItemPojo.getQuantity(), productPojo);
            } catch (Exception exception) {
                errors.append(addToErrorList(exception.getMessage()));
            }
        }
        throwApiErrorsList(errors);
        Integer orderId = 0;
        try {
            orderId = orderService.createOrder(orderItemPojoList);
        } catch (Exception exception) {
            errors.append(addToErrorList(exception.getMessage()));
        }
        throwApiErrorsList(errors);
        return orderId;
    }

    public void updateOrder(Integer orderId, List<OrderItemPojo> orderItemPojoList, List<String> barcodeList) throws ApiException {
        StringBuilder errors = new StringBuilder();
        if (orderItemPojoList.isEmpty())
            throw new ApiException("Add at least one item to order!");
        Set<String> set = new HashSet<>(barcodeList);
        if (set.size() < barcodeList.size()) {
            throw new ApiException("There are some duplicate order items!");
        }
        deleteOldItems(orderId);
        int i = 0;
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            try {
                orderItemPojo.setProductId(productService.getIdFromBarcode(barcodeList.get(i++)));
                ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
                if (productPojo.getMrp() < orderItemPojo.getSellingPrice())
                    throw new ApiException("Selling Price cannot be greater than MRP Rs." + productPojo.getMrp() + " for barcode: " + productPojo.getBarcode());
                inventoryService.updateInventoryOnOrder(orderItemPojo.getQuantity(), productPojo);
            } catch (Exception exception) {
                errors.append(addToErrorList(exception.getMessage()));
            }
        }
        throwApiErrorsList(errors);
        try {
            orderService.updateOrder(orderItemPojoList);
        } catch (Exception exception) {
            errors.append(addToErrorList(exception.getMessage()));
        }
        throwApiErrorsList(errors);
    }

    private void deleteOldItems(Integer orderId) throws ApiException {
        List<OrderItemPojo> exorderItemPojoList = orderService.getOrderItemsByOrderId(orderId);
        for (OrderItemPojo exorderItemPojo : exorderItemPojoList) {
            try {
                orderService.deleteOrderItem(exorderItemPojo.getId());
                ProductPojo productPojo = productService.get(exorderItemPojo.getProductId());
                inventoryService.updateInventoryOnOrder(-exorderItemPojo.getQuantity(), productPojo);
            } catch (Exception exception) {
                throw new ApiException("Unable to delete previous order items!");
            }
        }
    }

}

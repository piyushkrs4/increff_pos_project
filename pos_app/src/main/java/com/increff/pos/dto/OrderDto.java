package com.increff.pos.dto;

import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.util.Converter.convertGeneric;
import static com.increff.pos.util.Converter.convertOrderPojoToData;
import static com.increff.pos.util.NormalizerUtil.normalize;
import static com.increff.pos.util.ValidatorUtil.validate;

@Component
public class OrderDto {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;

    public List<OrderData> getAllOrders() {
        List<OrderPojo> orderPojoList = orderService.getAllOrders();
        List<OrderData> orderDataList = new ArrayList<>();
        for (OrderPojo orderPojo : orderPojoList) {
            orderDataList.add(convertOrderPojoToData(orderPojo));
        }
        return orderDataList;
    }

    public void addOrder(List<OrderItemForm> orderItemFormList) throws ApiException, IllegalAccessException {
        if (orderItemFormList.size() == 0)
            throw new ApiException("Please add at least one item to place order");
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        for (OrderItemForm orderItemForm : orderItemFormList) {
            validate(orderItemForm);
            normalize(orderItemForm);
            OrderItemPojo orderItemPojo = convertGeneric(orderItemForm, OrderItemPojo.class);
            orderItemPojo.setProductId(productService.getIdFromBarcode(orderItemForm.getBarcode()));
            orderItemPojoList.add(orderItemPojo);
        }
        orderService.addOrder(orderItemPojoList);
    }

    public List<OrderItemData> getOrderItemsByOrderId(Integer orderItemId) throws ApiException {
        List<OrderItemPojo> orderItemPojoList = orderService.getOrderItemsByOrderId(orderItemId);
        List<OrderItemData> orderItemDataList = new ArrayList<>();
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            OrderItemData orderItemData = convertGeneric(orderItemPojo, OrderItemData.class);
            ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
            orderItemData.setBarcode(productPojo.getBarcode());
            orderItemDataList.add(orderItemData);
        }
        return orderItemDataList;
    }


    public void updateOrder(Integer orderId, List<OrderItemForm> orderItemFormList) throws ApiException, IllegalAccessException {
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        for (OrderItemForm orderItemForm : orderItemFormList) {
            validate(orderItemForm);
            normalize(orderItemForm);
            OrderItemPojo orderItemPojo = convertGeneric(orderItemForm, OrderItemPojo.class);
            orderItemPojo.setProductId(productService.getIdFromBarcode(orderItemForm.getBarcode()));
            orderItemPojo.setOrderId(orderId);
            orderItemPojoList.add(orderItemPojo);
        }
        orderService.updateOrder(orderId, orderItemPojoList);
    }

}

// Todo -> dto to dto call not allowed
// Todo -> add flow layer for transaction in multiple pojos


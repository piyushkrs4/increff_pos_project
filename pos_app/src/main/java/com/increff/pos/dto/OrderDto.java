package com.increff.pos.dto;

import com.increff.pos.flow.OrderFlow;
import com.increff.pos.model.datas.OrderData;
import com.increff.pos.model.datas.OrderItemData;
import com.increff.pos.model.forms.OrderItemForm;
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
import static com.increff.pos.util.GenericFunctionsUtil.*;
import static com.increff.pos.util.NormalizerUtil.normalize;
import static com.increff.pos.util.ValidatorUtil.validate;

@Component
public class OrderDto {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderFlow orderFlow;
    @Autowired
    private ProductService productService;

    public Integer addOrder(List<OrderItemForm> orderItemFormList) throws ApiException {
        StringBuilder errors = new StringBuilder();
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        List<String> barcodeList = new ArrayList<>();
        for (OrderItemForm orderItemForm : orderItemFormList) {
            try {
                validate(orderItemForm);
                normalize(orderItemForm);
                orderItemForm.setSellingPrice(roundUpTo2DecimalPlaces(orderItemForm.getSellingPrice()));
                OrderItemPojo orderItemPojo = convertGeneric(orderItemForm, OrderItemPojo.class);
                barcodeList.add(orderItemForm.getBarcode());
                orderItemPojoList.add(orderItemPojo);
            } catch (Exception exception) {
                errors.append(addToErrorList(exception.getMessage()));
            }
        }
        throwApiErrorsList(errors);
        checkOverFlow(orderItemPojoList);
        Integer orderId = 0;
        try {
            orderId = orderFlow.addOrder(orderItemPojoList, barcodeList);
        } catch (Exception exception) {
            errors.append(addToErrorList(exception.getMessage()));
        }
        throwApiErrorsList(errors);
        return orderId;
    }

    public List<OrderData> getAllOrders() {
        List<OrderPojo> orderPojoList = orderService.getAllOrders();
        List<OrderData> orderDataList = new ArrayList<>();
        for (OrderPojo orderPojo : orderPojoList) {
            orderDataList.add(convertOrderPojoToData(orderPojo));
        }
        return orderDataList;
    }

    public List<OrderItemData> getOrderItemsByOrderId(Integer orderId) throws ApiException {
        List<OrderItemPojo> orderItemPojoList = orderService.getOrderItemsByOrderId(orderId);
        List<OrderItemData> orderItemDataList = new ArrayList<>();
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            OrderItemData orderItemData = convertGeneric(orderItemPojo, OrderItemData.class);
            ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
            orderItemData.setBarcode(productPojo.getBarcode());
            orderItemData.setProductName(productPojo.getName());
            orderItemDataList.add(orderItemData);
        }
        return orderItemDataList;
    }


    public void updateOrder(Integer orderId, List<OrderItemForm> orderItemFormList) throws ApiException {
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        StringBuilder errors = new StringBuilder();
        List<String> barcodeList = new ArrayList<>();
        for (OrderItemForm orderItemForm : orderItemFormList) {
            try {
                validate(orderItemForm);
                normalize(orderItemForm);
                orderItemForm.setSellingPrice(roundUpTo2DecimalPlaces(orderItemForm.getSellingPrice()));
                OrderItemPojo orderItemPojo = convertGeneric(orderItemForm, OrderItemPojo.class);
                orderItemPojo.setOrderId(orderId);
                barcodeList.add(orderItemForm.getBarcode());
                orderItemPojoList.add(orderItemPojo);
            } catch (Exception exception) {
                errors.append(addToErrorList(exception.getMessage()));
            }
        }
        throwApiErrorsList(errors);
        checkOverFlow(orderItemPojoList);
        try {
            orderFlow.updateOrder(orderId, orderItemPojoList, barcodeList);
        } catch (Exception exception) {
            errors.append(addToErrorList(exception.getMessage()));
        }
        throwApiErrorsList(errors);
    }

}


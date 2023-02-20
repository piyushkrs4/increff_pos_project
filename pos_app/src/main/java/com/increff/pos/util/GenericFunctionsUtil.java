package com.increff.pos.util;

import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.service.ApiException;
import com.opencsv.CSVWriter;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.List;

public class GenericFunctionsUtil {
    public static final Double MAX_PRICE = 2000000000.0;

    public static String addToErrorList(String exception) {
        return exception + "\n";
    }

    public static void throwApiErrorsList(StringBuilder errors) throws ApiException {
        String errorsString = String.valueOf(errors);
        if (!errorsString.isEmpty())
            throw new ApiException(errorsString);
    }

    public static CSVWriter createCSVWriter(ByteArrayOutputStream byteArrayOutputStream) {
        return new CSVWriter(new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8),
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END);
    }

    public static Double roundUpTo2DecimalPlaces(Double value) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return Double.valueOf(decimalFormat.format(value));
    }

    public static void checkOverFlow(List<OrderItemPojo> orderItemPojoList) throws ApiException {
        Double totalPrice = 0.0;
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            totalPrice += orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice();
            if (totalPrice > MAX_PRICE) {
                throw new ApiException("Cannot order with total price more than 2000000000!");
            }
        }
    }
}

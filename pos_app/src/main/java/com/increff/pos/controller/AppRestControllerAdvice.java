package com.increff.pos.controller;

import com.increff.pos.model.datas.MessageData;
import com.increff.pos.service.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class AppRestControllerAdvice {

    @ExceptionHandler(ApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageData handle(ApiException apiException) {
        MessageData messageData = new MessageData();
        messageData.setMessage(apiException.getMessage());
        return messageData;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MessageData handle(Throwable throwable) {
        MessageData messageData = new MessageData();
        messageData.setMessage("An unknown error has occurred - " + throwable.getMessage());
        return messageData;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final MessageData handleConstraintViolation(ConstraintViolationException constraintViolationException) {
        List<String> details = constraintViolationException.getConstraintViolations()
                .parallelStream()
                .map(e -> e.getPropertyPath() + " " + e.getMessage())
                .collect(Collectors.toList());

        MessageData messageData = new MessageData();

        messageData.setMessage(details.toString());
        return messageData;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageData handleException(HttpMessageNotReadableException httpMessageNotReadableException) {
        MessageData data = new MessageData();
        data.setMessage("Invalid inputs");
        return data;
    }
}
package com.increff.pos.controller;

import com.increff.pos.model.MessageData;
import com.increff.pos.service.ApiException;
import org.springframework.http.HttpStatus;
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
	public MessageData handle(ApiException e) {
		MessageData data = new MessageData();
		data.setMessage(e.getMessage());
		return data;
	}

	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public MessageData handle(Throwable e) {
		MessageData data = new MessageData();
		data.setMessage("An unknown error has occurred - " + e.getMessage());
		return data;
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public final MessageData handleConstraintViolation(ConstraintViolationException ex) {
		List<String> details = ex.getConstraintViolations()
				.parallelStream()
				.map(e -> e.getPropertyPath() +" " + e.getMessage())
				.collect(Collectors.toList());

		MessageData data = new MessageData();

		data.setMessage(details.toString());
		return data;
	}
}
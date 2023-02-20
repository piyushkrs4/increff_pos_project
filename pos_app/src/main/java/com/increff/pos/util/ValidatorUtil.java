package com.increff.pos.util;

import com.increff.pos.service.ApiException;

import javax.validation.*;
import java.util.Set;

public class ValidatorUtil {
    public static <T> void validate(T form) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(form);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public static void validateDates(String startDate, String endDate) throws ApiException {
        if (startDate.compareTo(endDate) > 0)
            throw new ApiException("Start date cannot be greater than end date!");
    }
}

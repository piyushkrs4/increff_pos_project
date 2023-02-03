package com.increff.pos.dto;

import com.increff.pos.model.SignupForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.increff.pos.util.Converter.convertGeneric;
import static com.increff.pos.util.NormalizerUtil.normalize;
import static com.increff.pos.util.ValidatorUtil.validate;

@Component
public class SignupDto {
    @Autowired
    private UserService userService;

    public void add(SignupForm signupForm) throws ApiException, IllegalAccessException {
        validate(signupForm);
        normalize(signupForm);
        UserPojo userPojo = convertGeneric(signupForm, UserPojo.class);
        userService.assignRole(userPojo);
    }


}

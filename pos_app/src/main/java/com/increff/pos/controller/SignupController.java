package com.increff.pos.controller;

import com.increff.pos.dto.SignupDto;
import com.increff.pos.model.SignupForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class SignupController {

    @Autowired
    private SignupDto signupDto;

    @ApiOperation(value = "Signs up a user")
    @RequestMapping(path = "/session/signup", method = RequestMethod.POST)
    public void signup(@RequestBody SignupForm signupForm) throws ApiException, IllegalAccessException {
        signupDto.add(signupForm);
    }

}

package com.increff.pos.controller;

import com.increff.pos.dto.AboutApiDto;
import com.increff.pos.model.datas.AboutAppData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class AboutApiController {

    @Autowired
    private AboutApiDto aboutApiDto;

    @ApiOperation(value = "Gives application name and version")
    @RequestMapping(path = "/api/about", method = RequestMethod.GET)
    public AboutAppData getDetails() {
        return aboutApiDto.getDetails();
    }

}

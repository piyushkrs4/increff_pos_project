package com.increff.pos.controller;

import com.increff.pos.service.ApiException;
import com.increff.pos.util.IOUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Controller
public class SampleController {
    @RequestMapping(value = "/sample/{fileName:.+}", method = RequestMethod.GET)
    public void getFile(@PathVariable("fileName") String fileName, HttpServletResponse response) throws ApiException {
        response.setContentType("text/csv");
        response.addHeader("Content-disposition:", "attachment; filename=" + fileName);
        String fileClasspath = "/TSVFiles/" + fileName;
        InputStream is = SampleController.class.getResourceAsStream(fileClasspath);
        if (Objects.isNull(is))
            throw new ApiException("Empty File");
        try {
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            throw new ApiException(e.getMessage());
        } finally {
            IOUtil.closeQuietly(is);
        }

    }
}

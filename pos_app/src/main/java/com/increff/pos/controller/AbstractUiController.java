package com.increff.pos.controller;

import com.increff.pos.model.InfoData;
import com.increff.pos.util.SecurityUtil;
import com.increff.pos.util.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

@Controller
public abstract class AbstractUiController {

    @Autowired
    private InfoData info;

    @Value("${app.baseUrl}")
    private String baseUrl;

    protected ModelAndView mav(String page) {
        // Get current user
        UserPrincipal principal = SecurityUtil.getPrincipal();

        info.setEmail(principal == null ? "" : principal.getEmail());

        // Set info
        ModelAndView mav = new ModelAndView(page);
        mav.addObject("info", info);
        mav.addObject("baseUrl", baseUrl);
        mav.addObject("role", principal == null ? "" : principal.getRole());
        return mav;
    }

    protected ModelAndView mav(String page, Integer id) {
        // Get current user
        UserPrincipal principal = SecurityUtil.getPrincipal();

        info.setEmail(principal == null ? "" : principal.getEmail());

        // Set info
        ModelAndView mav = new ModelAndView(page);
        mav.addObject("info", info);
        mav.addObject("baseUrl", baseUrl);
        mav.addObject("role", principal == null ? "" : principal.getRole());
        mav.addObject("orderId", id);
        return mav;
    }

}

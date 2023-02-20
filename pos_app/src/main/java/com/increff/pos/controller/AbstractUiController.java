package com.increff.pos.controller;

import com.increff.pos.model.datas.InfoData;
import com.increff.pos.model.datas.UserPrincipal;
import com.increff.pos.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

@Controller
public abstract class AbstractUiController {

    @Autowired
    private InfoData infoData;

    @Value("${app.baseUrl}")
    private String baseUrl;

    @Value("${supervisor.email}")
    private String supervisorEmail;

    protected ModelAndView mav(String page) {
        UserPrincipal principal = SecurityUtil.getPrincipal();

        infoData.setEmail(principal == null ? "" : principal.getEmail());

        if(!infoData.getStatus()){
            infoData.setStatus(true);
        }
        else{
            infoData.setMessage("");
        }

        ModelAndView mav = new ModelAndView(page);
        mav.addObject("info", infoData);
        mav.addObject("baseUrl", baseUrl);
        mav.addObject("role", principal == null ? "" : principal.getRole());
        return mav;
    }

    protected ModelAndView mav() {
        UserPrincipal principal = SecurityUtil.getPrincipal();

        infoData.setEmail(principal == null ? "" : principal.getEmail());

        ModelAndView mav = new ModelAndView("user.html");
        mav.addObject("info", infoData);
        mav.addObject("baseUrl", baseUrl);
        mav.addObject("role", principal == null ? "" : principal.getRole());
        mav.addObject("supervisorEmail", supervisorEmail);
        return mav;
    }

}

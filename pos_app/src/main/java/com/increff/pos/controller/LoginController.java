package com.increff.pos.controller;

import com.increff.pos.dto.UserDto;
import com.increff.pos.model.datas.InfoData;
import com.increff.pos.model.datas.UserPrincipal;
import com.increff.pos.model.forms.LoginForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.SecurityUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Objects;

import static com.increff.pos.util.NormalizerUtil.normalize;
import static com.increff.pos.util.ValidatorUtil.validate;

@Controller
@RequestMapping(path = "/session")
public class LoginController {

    @Autowired
    private UserDto userDto;
    @Autowired
    private InfoData info;

    @ApiOperation(value = "Logs in a user")
    @RequestMapping(path = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView login(HttpServletRequest httpServletRequest, LoginForm f) throws ApiException, IllegalAccessException {
        validate(f);
        normalize(f);
        UserPojo p = userDto.getUsingEmail(f.getEmail());
        boolean authenticated = (p != null && Objects.equals(p.getPassword(), f.getPassword()));
        if (!authenticated) {
            info.setMessage("Invalid username or password");
            info.setStatus(false);
            return new ModelAndView("redirect:/site/login");
        }

        // Create authentication object
        Authentication authentication = convert(p);
        // Create new session
        HttpSession session = httpServletRequest.getSession(true);
        // Attach Spring SecurityContext to this new session
        SecurityUtil.createContext(session);
        // Attach Authentication object to the Security Context
        SecurityUtil.setAuthentication(authentication);

        return new ModelAndView("redirect:/ui/home");

    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        return new ModelAndView("redirect:/site/logout");
    }

    @ApiOperation(value = "Signs up a user")
    @RequestMapping(path = "/signup", method = RequestMethod.POST)
    public ModelAndView signup(@RequestBody LoginForm loginForm) throws ApiException, IllegalAccessException {
        userDto.addUsingSingUp(loginForm);
        return new ModelAndView("redirect:/site/logout");
    }

    private static Authentication convert(UserPojo userPojo) {
        // Create principal
        UserPrincipal principal = new UserPrincipal();
        principal.setEmail(userPojo.getEmail());
        principal.setId(userPojo.getId());
        principal.setRole(userPojo.getRole());

        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userPojo.getRole()));

        return new UsernamePasswordAuthenticationToken(principal, null,
                authorities);
    }

}

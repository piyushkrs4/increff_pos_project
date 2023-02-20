package com.increff.pos.dto;

import com.increff.pos.model.datas.UserData;
import com.increff.pos.model.forms.LoginForm;
import com.increff.pos.model.forms.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.increff.pos.util.Converter.convertGeneric;
import static com.increff.pos.util.NormalizerUtil.normalize;
import static com.increff.pos.util.ValidatorUtil.validate;

@Component
public class UserDto {
    private static final String SUPERVISOR = "supervisor";
    private static final String OPERATOR = "operator";
    @Autowired
    private UserService userService;

    public void addUser(UserForm userForm) throws ApiException, IllegalAccessException {
        validate(userForm);
        normalize(userForm.getEmail());
        normalize(userForm.getRole());
        if (Objects.equals(SUPERVISOR, userForm.getRole()) || Objects.equals(OPERATOR, userForm.getRole())) {
            UserPojo brandPojo = convertGeneric(userForm, UserPojo.class);
            userService.addUser(brandPojo);
        } else {
            throw new ApiException("Invalid Role");
        }

    }

    public void addUsingSingUp(LoginForm loginForm) throws ApiException, IllegalAccessException {
        validate(loginForm);
        normalize(loginForm);
        UserPojo userPojo = convertGeneric(loginForm, UserPojo.class);
        userService.assignRole(userPojo);
    }

    public void updateUser(Integer userId, UserForm userForm) throws ApiException, IllegalAccessException {
        validate(userForm);
        normalize(userForm);
        if (Objects.equals(SUPERVISOR, userForm.getRole()) || Objects.equals(OPERATOR, userForm.getRole())) {
            UserPojo brandPojo = convertGeneric(userForm, UserPojo.class);
            userService.updateUser(userId, brandPojo);
        } else {
            throw new ApiException("Invalid Role");
        }
    }

    public void delete(Integer userId) throws ApiException {
        userService.delete(userId);
    }

    public UserData get(Integer userId) throws ApiException {
        return convertGeneric(userService.get(userId), UserData.class);
    }

    public UserPojo getUsingEmail(String email) {
        return userService.getUsingEmail(email);
    }

    public List<UserData> getAll() throws ApiException {
        List<UserPojo> userPojoList = userService.getAll();
        List<UserData> userDataList = new ArrayList<>();
        for (UserPojo userPojo : userPojoList) {
            userDataList.add(convertGeneric(userPojo, UserData.class));
        }
        return userDataList;
    }
}

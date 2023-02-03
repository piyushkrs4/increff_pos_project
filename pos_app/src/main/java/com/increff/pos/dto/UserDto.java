package com.increff.pos.dto;

import com.increff.pos.model.UserData;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.util.Converter.convertGeneric;
import static com.increff.pos.util.NormalizerUtil.normalize;
import static com.increff.pos.util.ValidatorUtil.validate;

@Component
public class UserDto {
    @Autowired
    private UserService userService;

    public void add(UserForm userForm) throws ApiException, IllegalAccessException {
        validate(userForm);
        normalize(userForm);
        UserPojo brandPojo = convertGeneric(userForm, UserPojo.class);
        userService.add(brandPojo);
    }

    public void delete(Integer userId) {
        userService.delete(userId);
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

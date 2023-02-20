package com.increff.pos.dto;

import com.increff.pos.model.datas.UserData;
import com.increff.pos.model.forms.LoginForm;
import com.increff.pos.model.forms.UserForm;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class UserDtoTest extends AbstractUnitTest {
    @Autowired
    private UserDto userDto;

    @Test
    public void testAddUser() throws ApiException, IllegalAccessException {
        UserForm userForm = new UserForm();
        userForm.setEmail("test@gmail.com");
        userForm.setRole("operator");
        userForm.setPassword("12345678");
        userDto.addUser(userForm);
        UserData userData = userDto.getAll().get(0);
        assertEquals("test@gmail.com", userData.getEmail());
        assertEquals("operator", userData.getRole());
        assertEquals("12345678", userData.getPassword());
    }

    @Test
    public void addUsingSingUp() throws ApiException, IllegalAccessException {
        LoginForm loginForm = new LoginForm();
        loginForm.setEmail("piyush.kumar@gmail.com");
        loginForm.setPassword("12345678");
        userDto.addUsingSingUp(loginForm);
        UserData userData = userDto.getAll().get(0);
        assertEquals("piyush.kumar@gmail.com", userData.getEmail());
        assertEquals("operator", userData.getRole());
        assertEquals("12345678", userData.getPassword());
    }

    @Test(expected = ApiException.class)
    public void testUpdateUser() throws ApiException, IllegalAccessException {
        UserForm userForm = new UserForm();
        userForm.setEmail("test@gmail.com");
        userForm.setRole("operator");
        userForm.setPassword("12345678");
        userDto.addUser(userForm);
        UserData userData = userDto.getAll().get(0);
        userForm.setPassword("88888888");
        userDto.updateUser(userData.getId(), userForm);
    }




}

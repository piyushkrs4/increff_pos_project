package com.increff.pos.service;

import com.increff.pos.dao.UserDao;
import com.increff.pos.pojo.UserPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserDao userDao;

    @Value("${supervisor.email}")
    private String supervisorEmail;

    public void add(UserPojo userPojo) throws ApiException {
        UserPojo existing = userDao.select(userPojo.getEmail());
        if (existing != null) {
            throw new ApiException("User with given email already exists");
        }
        userDao.insert(userPojo);
    }

    public void assignRole(UserPojo userPojo) throws ApiException {
        if (userPojo.getEmail().equals(supervisorEmail)) {
            userPojo.setRole("supervisor");
        } else
            userPojo.setRole("operator");
        add(userPojo);
    }

    public UserPojo get(String email) throws ApiException {
        return userDao.select(email);
    }

    public List<UserPojo> getAll() {
        return userDao.selectAll(UserPojo.class);
    }

    public void delete(Integer userId) {
        userDao.delete(userId, UserPojo.class);
    }
}

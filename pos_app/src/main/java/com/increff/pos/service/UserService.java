package com.increff.pos.service;

import com.increff.pos.dao.UserDao;
import com.increff.pos.model.datas.UserPrincipal;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserDao userDao;

    @Value("${supervisor.email}")
    private String supervisorEmail;

    public void addUser(UserPojo userPojo) throws ApiException {
        UserPojo existing = userDao.select(userPojo.getEmail());
        if (Objects.nonNull(existing)) {
            throw new ApiException("User with given email already exists");
        }
        userDao.insert(userPojo);
    }

    public void updateUser(Integer userId, UserPojo userPojo) throws ApiException {
        UserPojo existing = getChecked(userId);
        UserPrincipal principal = SecurityUtil.getPrincipal();
        if(Objects.isNull(principal))
            throw new ApiException("Unable to edit user");
        if (userPojo.getEmail().equals(supervisorEmail)) {
            if (principal.getEmail().equals(supervisorEmail)) {
                existing.setPassword(userPojo.getPassword());
            } else
                throw new ApiException("You cannot edit this supervisor!");
        } else {
            existing.setPassword(userPojo.getPassword());
            existing.setRole(userPojo.getRole());
        }
    }

    public void assignRole(UserPojo userPojo) throws ApiException {
        if (userPojo.getEmail().equals(supervisorEmail)) {
            userPojo.setRole("supervisor");
        } else{
            userPojo.setRole("operator");
        }
        addUser(userPojo);
    }

    public UserPojo get(Integer userId) throws ApiException {
        return getChecked(userId);
    }

    public UserPojo getUsingEmail(String email) {
        return userDao.select(email);
    }


    public List<UserPojo> getAll() {
        return userDao.selectAll(UserPojo.class);
    }

    public void delete(Integer userId) throws ApiException {
        UserPojo userPojo = getChecked(userId);
        UserPrincipal principal = SecurityUtil.getPrincipal();
        if(Objects.isNull(principal))
            throw new ApiException("Unable to edit user");
        if (userPojo.getEmail().equals(supervisorEmail)) {
            throw new ApiException("You cannot delete this user!");
        } else if (userPojo.getEmail().equals(principal.getEmail())) {
            throw new ApiException("You cannot delete your account!");
        }
        userDao.delete(userId, UserPojo.class);
    }

    private UserPojo getChecked(Integer userId) throws ApiException {
        UserPojo userPojo = userDao.select(userId, UserPojo.class);
        if (Objects.isNull(userPojo)) {
            throw new ApiException("User does not exist");
        }
        return userPojo;
    }
}

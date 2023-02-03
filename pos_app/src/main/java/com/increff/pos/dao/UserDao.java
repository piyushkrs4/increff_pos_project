package com.increff.pos.dao;

import com.increff.pos.pojo.UserPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class UserDao extends AbstractDao {
    private static String select_email = "select p from UserPojo p where email=:email";

    public UserPojo select(String email) {
        TypedQuery<UserPojo> query = getQuery(select_email, UserPojo.class);
        query.setParameter("email", email);
        return getSingle(query);
    }
}

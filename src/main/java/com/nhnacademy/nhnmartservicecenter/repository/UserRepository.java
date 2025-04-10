package com.nhnacademy.nhnmartservicecenter.repository;

import com.nhnacademy.nhnmartservicecenter.domain.User;

import java.util.List;

public interface UserRepository {
    boolean exists(String id);
    boolean match(String id, String password);
    List<User> getUserList();
    User getUser(String id);
    void addUser(User user);
    void modify(String id,User user);
}

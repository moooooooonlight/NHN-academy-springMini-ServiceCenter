package com.nhnacademy.nhnmartservicecenter.repository;

import com.nhnacademy.nhnmartservicecenter.domain.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Repository
public class UserRepositoryImpl implements UserRepository{

    Map<String, User> userMap;

    public UserRepositoryImpl() {
        userMap = new ConcurrentHashMap<>();
        userMap.put("123", new User("123","123","123", User.auth.ADMIN));
        userMap.put("444", new User("444","444","444", User.auth.CUSTOM));
        userMap.put("555", new User("555","555","555", User.auth.CUSTOM));
        userMap.put("666", new User("666","666","666", User.auth.CUSTOM));
        userMap.put("777", new User("777","777","777", User.auth.CUSTOM));
        userMap.put("888", new User("888","888","888", User.auth.CUSTOM));
        userMap.put("999", new User("999","999","999", User.auth.CUSTOM));
    }

    @Override
    public boolean exists(String id) {
        return userMap.containsKey(id);
    }

    @Override
    public boolean match(String id, String password) {
        if(!exists(id)){
            return false;
        }

        User user = userMap.get(id);
        return user.getPassword().equals(password);
    }

    @Override
    public List<User> getUserList() {
        List<User> values = (List<User>) userMap.values();
        return values;
    }

    @Override
    public User getUser(String id) {
        if(!exists(id)){
            return null;
        }
        return userMap.get(id);
    }

    @Override
    public void addUser(User user) {
        if(exists(user.getId())){
            // 이미 존재하는 id이다.
        }
        userMap.put(user.getId(),user);
    }

    @Override
    public void modify(String id, User user) {
        if(!exists(id)){
            // 존재하지 않는 user입니다.
        }

        userMap.put(id, user);
    }
}

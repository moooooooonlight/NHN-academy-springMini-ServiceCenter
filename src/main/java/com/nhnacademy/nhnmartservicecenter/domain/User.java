package com.nhnacademy.nhnmartservicecenter.domain;

import lombok.Getter;

@Getter
public class User {
    public enum auth{
        ADMIN,
        CUSTOM
    }

    private String id;
    private String password;
    private String name;
    private auth auth;


    public User(String id, String password, String name, User.auth auth) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.auth = auth;
    }
}

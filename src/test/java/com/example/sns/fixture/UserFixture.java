package com.example.sns.fixture;

import com.example.sns.entity.User;

public class UserFixture {

    public static User get(String username, String password) {
        return User.of(username, password, "ROLE_ADMIN");
    }
}

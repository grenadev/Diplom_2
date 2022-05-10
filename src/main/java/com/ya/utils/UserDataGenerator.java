package com.ya.utils;

import com.ya.model.User;
import org.apache.commons.lang3.RandomStringUtils;

public class UserDataGenerator {
    public static User generateUserData() {
        String userEmail = RandomStringUtils.randomAlphabetic(10) + "@mail.ru";
        String userPassword = RandomStringUtils.randomAlphabetic(10);
        String userName = RandomStringUtils.randomAlphabetic(10);

        User user = new User(userEmail, userPassword, userName);

        return user;
    }
}

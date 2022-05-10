package com.ya.tests;

import com.ya.client.UserClient;
import com.ya.model.User;
import com.ya.utils.UserDataGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class UserLoginTests {

    UserDataGenerator generator;
    User user;
    UserClient userClient;
    String auth;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = generator.generateUserData();
    }

    @After
    public void tearDown() {
        if (auth != null) {
            userClient.deleteUser(auth);
        }
    }

    @Test
    @DisplayName("User login")
    public void loginExistsUser() {
        auth = userClient.createNewUser(user).extract().body().path("accessToken");
        ValidatableResponse loginResponse = userClient.loginUser(user);
        int statusCode = loginResponse.extract().statusCode();
        boolean status = loginResponse.extract().body().path("success");

        assertThat("Response status not 200", statusCode, equalTo(200));
        assertThat("Response body does not contain success status ", status, equalTo(true));
    }

    @Test
    @DisplayName("Not exists user login")
    public void loginNonExistsUser() {
        auth = userClient.createNewUser(user).extract().body().path("accessToken");
        user.setPassword(user.getPassword() + "test");
        ValidatableResponse loginResponse = userClient.loginUser(user);
        int statusCode = loginResponse.extract().statusCode();
        String errorStatus = loginResponse.extract().body().path("message");

        assertThat("Response status not 401", statusCode, equalTo(401));
        assertThat("Response body does not contain error message", errorStatus, equalTo("email or password are incorrect"));
    }
}

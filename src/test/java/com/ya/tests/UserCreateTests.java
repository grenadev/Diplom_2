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

public class UserCreateTests {

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
    @DisplayName("Create new user")
    public void createUser() {
        ValidatableResponse loginResponse = userClient.createNewUser(user);
        int statusCode = loginResponse.extract().statusCode();
        boolean errorStatus = loginResponse.extract().body().path("success");
        auth = loginResponse.extract().body().path("accessToken");

        assertThat("Response status not 200", statusCode, equalTo(200));
        assertThat("Response body does not contain success status ", errorStatus, equalTo(true));
    }

    @Test
    @DisplayName("User already exists")
    public void createAlreadyExistsUser() {
        auth = userClient.createNewUser(user).extract().body().path("accessToken");
        ValidatableResponse loginResponse = userClient.createNewUser(user);
        int statusCode = loginResponse.extract().statusCode();
        String errorStatus = loginResponse.extract().body().path("message");

        assertThat("Response status not 403", statusCode, equalTo(403));
        assertThat("Response body does not contain error message", errorStatus, equalTo("User already exists"));
    }

    @Test
    @DisplayName("Create user without name")
    public void createUserWithoutName() {
        user.setName("");
        ValidatableResponse loginResponse = userClient.createNewUser(user);
        int statusCode = loginResponse.extract().statusCode();
        String errorStatus = loginResponse.extract().body().path("message");

        assertThat("Response status not 403", statusCode, equalTo(403));
        assertThat("Response body does not contain error message", errorStatus, equalTo("Email, password and name are required fields"));
    }
}

package com.ya.tests;

import com.ya.client.UserClient;
import com.ya.model.User;
import com.ya.utils.UserDataGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class UserEditDataTests {

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
    @DisplayName("User edit email with Token")
    public void editEmailWithToken() {
        ValidatableResponse loginResponse = userClient.createNewUser(user);
        auth = loginResponse.extract().body().path("accessToken");
        user.setEmail(RandomStringUtils.randomAlphabetic(10) + "@mail.ru");
        userClient.editUserWithToken(user, auth);
        ValidatableResponse editResponse = userClient.loginUser(user);
        int statusCode = editResponse.extract().statusCode();
        String newEmail = editResponse.extract().body().path("user.email");

        assertThat("Response status not 200", statusCode, equalTo(200));
        assertThat("Users email in request/response are different", newEmail, equalTo((user.getEmail().toLowerCase())));
    }

    @Test
    @DisplayName("User edit password with Token")
    public void editPasswordWithToken() {
        ValidatableResponse loginResponse = userClient.createNewUser(user);
        auth = loginResponse.extract().body().path("accessToken");
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        userClient.editUserWithToken(user, auth);
        ValidatableResponse editResponse = userClient.loginUser(user);
        int statusCode = editResponse.extract().statusCode();
        String newEmail = editResponse.extract().body().path("user.email");

        assertThat("Response status not 200", statusCode, equalTo(200));
        assertThat("Users email in request/response are different", newEmail, equalTo((user.getEmail().toLowerCase())));
    }

    @Test
    @DisplayName("User edit name with Token")
    public void editNameWithToken() {
        ValidatableResponse loginResponse = userClient.createNewUser(user);
        auth = loginResponse.extract().body().path("accessToken");
        user.setName("test");
        userClient.editUserWithToken(user, auth);
        ValidatableResponse editResponse = userClient.loginUser(user);
        int statusCode = editResponse.extract().statusCode();
        String newName = editResponse.extract().body().path("user.name");

        assertThat("Response status not 200", statusCode, equalTo(200));
        assertThat("Users email in request/response are different", newName, equalTo((user.getName().toLowerCase())));
    }

    @Test
    @DisplayName("User edit email without Token")
    public void editEmailWithoutToken() {
        auth = userClient.createNewUser(user).extract().body().path("accessToken");
        user.setEmail(RandomStringUtils.randomAlphabetic(10) + "@mail.ru");
        ValidatableResponse editResponse = userClient.editUserWithoutToken(user);
        int statusCode = editResponse.extract().statusCode();
        String errorMessage = editResponse.extract().body().path("message");

        assertThat("Response status not 401", statusCode, equalTo(401));
        assertThat("Error message should be different", errorMessage, equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("User edit password without Token")
    public void editPasswordWithoutToken() {
        auth = userClient.createNewUser(user).extract().body().path("accessToken");
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        ValidatableResponse editResponse = userClient.editUserWithoutToken(user);
        int statusCode = editResponse.extract().statusCode();
        String errorMessage = editResponse.extract().body().path("message");

        assertThat("Response status not 401", statusCode, equalTo(401));
        assertThat("Error message should be different", errorMessage, equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("User edit name without Token")
    public void editNameWithoutToken() {
        auth = userClient.createNewUser(user).extract().body().path("accessToken");
        user.setName("test");
        ValidatableResponse editResponse = userClient.editUserWithoutToken(user);
        int statusCode = editResponse.extract().statusCode();
        String errorMessage = editResponse.extract().body().path("message");

        assertThat("Response status not 401", statusCode, equalTo(401));
        assertThat("Error message should be different", errorMessage, equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("User edit email but email already exists with Token")
    public void editWithAlreadyExistsEmail() {

        //Создаем первого пользователя
        ValidatableResponse loginResponse = userClient.createNewUser(user);
        auth = loginResponse.extract().body().path("accessToken");

        //создаем второго пользователя
        User another = generator.generateUserData();
        String anotherAuth = userClient.createNewUser(another).extract().body().path("accessToken");

        //Подменяем email первого пользователя на адрес второго
        user.setEmail(another.getEmail());

        //Пытаемся сохранить изменения данных пользователя №1
        ValidatableResponse editResponse = userClient.editUserWithToken(user, auth);

        //Удаляем пользователя созданного для того чтобы занять email
        userClient.deleteUser(anotherAuth);

        int statusCode = editResponse.extract().statusCode();
        String errorMessage = editResponse.extract().body().path("message");

        assertThat("Response status not 403", statusCode, equalTo(403));
        assertThat("Error message should be different", errorMessage, equalTo("User with such email already exists"));
    }


}

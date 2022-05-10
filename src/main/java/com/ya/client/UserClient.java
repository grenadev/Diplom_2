package com.ya.client;

import com.ya.model.User;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends StellarRestClient {
    private static final String CREATE_USER_PATH = "api/auth/register/";
    private static final String LOGIN_USER_PATH = "api/auth/login/";
    private static final String EDIT_USER_PATH = "api/auth/user/";

    @Step("Create new user")
    public ValidatableResponse createNewUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body("{\"email\":\"" + user.getEmail() + "\"," +
                        "\"name\":\"" + user.getName() + "\","
                        + "\"password\":\"" + user.getPassword() + "\"}")
                .when()
                .post(CREATE_USER_PATH)
                .then();
    }

    @Step("User login")
    public ValidatableResponse loginUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body("{\"email\":\"" + user.getEmail() + "\","
                        + "\"password\":\"" + user.getPassword() + "\"}")
                .when()
                .post(LOGIN_USER_PATH)
                .then();
    }

    @Step("Edit user data with token")
    public ValidatableResponse editUserWithToken(User user, String Authorization) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", Authorization)
                .body("{\"email\":\"" + user.getEmail() + "\"," +
                        "\"name\":\"" + user.getName() + "\","
                        + "\"password\":\"" + user.getPassword() + "\"}")
                .when()
                .patch(EDIT_USER_PATH)
                .then();
    }

    @Step("Edit user data without token")
    public ValidatableResponse editUserWithoutToken(User user) {
        return given()
                .spec(getBaseSpec())
                .body("{\"email\":\"" + user.getEmail() + "\"," +
                        "\"name\":\"" + user.getName() + "\","
                        + "\"password\":\"" + user.getPassword() + "\"}")
                .when()
                .patch(EDIT_USER_PATH)
                .then();
    }

    @Step("Delete user")
    public ValidatableResponse deleteUser(String auth) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", auth)
                .body("")
                .when()
                .patch(EDIT_USER_PATH)
                .then();
    }
}

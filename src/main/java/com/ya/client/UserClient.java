package com.ya.client;

import com.ya.model.User;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class UserClient extends StellarRestClient {
    private static final String CREATE_USER_PATH = "api/auth/register/";
    private static final String LOGIN_USER_PATH = "api/auth/login/";
    private static final String EDIT_USER_PATH = "api/auth/user/";

    @Step("Create new user")
    public ValidatableResponse createNewUser(User user) {

        HashMap<String,String> dataBody = new HashMap<String,String>();

        dataBody.put("email", user.getEmail());
        dataBody.put("name", user.getName());
        dataBody.put("password", user.getPassword());

        return given()
                .spec(getBaseSpec())
                .contentType(ContentType.JSON)
                .body(dataBody)
                .when()
                .post(CREATE_USER_PATH)
                .then();
    }

    @Step("User login")
    public ValidatableResponse loginUser(User user) {

        HashMap<String,String> dataBody = new HashMap<String,String>();

        dataBody.put("email", user.getEmail());
        dataBody.put("password", user.getPassword());

        return given()
                .spec(getBaseSpec())
                .body(dataBody)
                .when()
                .post(LOGIN_USER_PATH)
                .then();
    }

    @Step("Edit user data with token")
    public ValidatableResponse editUserWithToken(User user, String Authorization) {

        HashMap<String,String> dataBody = new HashMap<String,String>();

        dataBody.put("email", user.getEmail());
        dataBody.put("name", user.getName());
        dataBody.put("password", user.getPassword());

        return given()
                .spec(getBaseSpec())
                .header("Authorization", Authorization)
                .contentType(ContentType.JSON)
                .body(dataBody)
                .when()
                .patch(EDIT_USER_PATH)
                .then();
    }

    @Step("Edit user data without token")
    public ValidatableResponse editUserWithoutToken(User user) {

        HashMap<String,String> dataBody = new HashMap<String,String>();

        dataBody.put("email", user.getEmail());
        dataBody.put("name", user.getName());
        dataBody.put("password", user.getPassword());

        return given()
                .spec(getBaseSpec())
                .body(dataBody)
                .when()
                .patch(EDIT_USER_PATH)
                .then();
    }

    @Step("Delete user")
    public ValidatableResponse deleteUser(String auth) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", auth)
                .when()
                .delete(EDIT_USER_PATH)
                .then();
    }
}

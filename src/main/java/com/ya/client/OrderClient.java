package com.ya.client;

import com.ya.model.Order;
import com.ya.model.User;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends StellarRestClient {
    private static final String CREATE_ORDER_PATH = "api/orders";
    private static final String GET_INGREDIENTS_LIST = "api/ingredients";
    private static final String GET_USER_ORDER_LIST = "api/orders";

    @Step("Create new order")
    public ValidatableResponse createNewOrderWithoutToken(Order order) {
        return given()
                .spec(getBaseSpec())
                .body("{\"ingredients\": [\"" + order.getBun() + "\",\"" + order.getSouse() + "\",\"" + order.getMain() + "\"]}")
                .when()
                .post(CREATE_ORDER_PATH)
                .then();
    }

    @Step("Create new order without Token")
    public ValidatableResponse createNewOrderWithToken(Order order, String auth) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", auth)
                .body("{\"ingredients\": [\"" + order.getBun() + "\",\"" + order.getSouse() + "\",\"" + order.getMain() + "\"]}")
                .when()
                .post(CREATE_ORDER_PATH)
                .then();
    }

    @Step("Create new order")
    public ValidatableResponse createNewOrderWithoutIngredients() {
        return given()
                .spec(getBaseSpec())
                .body("{\"ingredients\": []}")
                .when()
                .post(CREATE_ORDER_PATH)
                .then();
    }

    @Step("Get ingredients list")
    public ValidatableResponse getIngredients() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(GET_INGREDIENTS_LIST)
                .then();
    }

    @Step("Get user order list")
    public ValidatableResponse getUserOrderList(String auth) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", auth)
                .when()
                .get(GET_USER_ORDER_LIST)
                .then();
    }

    @Step("Get order list without token")
    public ValidatableResponse getOrderListWithoutToken() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(GET_USER_ORDER_LIST)
                .then();
    }
}

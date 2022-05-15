package com.ya.client;

import com.ya.model.Order;
import com.ya.model.User;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import java.util.ArrayList;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class OrderClient extends StellarRestClient {
    private static final String CREATE_ORDER_PATH = "api/orders";
    private static final String GET_INGREDIENTS_LIST = "api/ingredients";
    private static final String GET_USER_ORDER_LIST = "api/orders";

    @Step("Create new order")
    public ValidatableResponse createNewOrderWithoutToken(Order order) {

        HashMap<String,Object> dataBody = new HashMap<String,Object>();

        ArrayList<String> ingredientsList = new ArrayList<String>();
        ingredientsList.add(order.getBun());
        ingredientsList.add(order.getSouse());
        ingredientsList.add(order.getMain());

        dataBody.put("ingredients", ingredientsList);


        return given()
                .spec(getBaseSpec())
                .contentType(ContentType.JSON)
                .body(dataBody)
                .when()
                .post(CREATE_ORDER_PATH)
                .then();
    }

    @Step("Create new order without Token")
    public ValidatableResponse createNewOrderWithToken(Order order, String auth) {

        HashMap<String,Object> dataBody = new HashMap<String,Object>();

        ArrayList<String> ingredientsList = new ArrayList<String>();
        ingredientsList.add(order.getBun());
        ingredientsList.add(order.getSouse());
        ingredientsList.add(order.getMain());

        dataBody.put("ingredients", ingredientsList);


        return given()
                .spec(getBaseSpec())
                .header("Authorization", auth)
                .contentType(ContentType.JSON)
                .body(dataBody)
                .when()
                .post(CREATE_ORDER_PATH)
                .then();
    }

    @Step("Create new order")
    public ValidatableResponse createNewOrderWithoutIngredients() {

        HashMap<String,Object> dataBody = new HashMap<String,Object>();

        ArrayList<String> ingredientsList = new ArrayList<String>();
        dataBody.put("ingredients", ingredientsList);

        return given()
                .spec(getBaseSpec())
                .contentType(ContentType.JSON)
                .body(dataBody)
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

package com.ya.tests;

import com.ya.client.OrderClient;
import com.ya.client.UserClient;
import com.ya.model.Order;
import com.ya.model.User;
import com.ya.utils.OrderDataGenerator;
import com.ya.utils.UserDataGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class UserOrderListTests {

    OrderDataGenerator orderGenerator;
    Order order;
    OrderClient orderClient;
    UserDataGenerator userGenerator;
    User user;
    UserClient userClient;
    String auth;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = userGenerator.generateUserData();
        orderClient = new OrderClient();
        order = orderGenerator.generateOrder();
    }

    @After
    public void tearDown() {
        if (auth != null) {
            userClient.deleteUser(auth);
        }
    }

    @Test
    @DisplayName("get order list without user token")
    public void getOrderListNonAuthUser() {

        ValidatableResponse orderListResponse = orderClient.getOrderListWithoutToken();

        //Проверка результата создания заказа
        int statusCode = orderListResponse.extract().statusCode();
        String status = orderListResponse.extract().body().path("message");

        assertThat("Response status not 401", statusCode, equalTo(401));
        assertThat("Response body does not contain correct error message ", status, equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("get users order list")
    public void getUsersOderList() {

        //Создание пользователя
        auth = userClient.createNewUser(user).extract().body().path("accessToken");

        //Создание двух заказов для пользователя
        orderClient.createNewOrderWithToken(order, auth);

        Order orderTwo = orderGenerator.generateOrder();
        orderClient.createNewOrderWithToken(orderTwo, auth);

        //Запрос данных по заказам пользователя

        ValidatableResponse orderListResponse = orderClient.getUserOrderList(auth);

        //Проверка получения списка заказов
        int statusCode = orderListResponse.extract().statusCode();
        int status = orderListResponse.extract().body().path("total");

        assertThat("Response status not 200", statusCode, equalTo(200));
        assertThat("Response body does not contain correct error message ", status, equalTo(2));
    }
}

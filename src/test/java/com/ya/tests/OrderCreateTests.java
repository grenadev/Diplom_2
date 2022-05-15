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

public class OrderCreateTests {

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
    @DisplayName("Auth user create order")
    public void createOrderTest() {

        //Создание пользователя под которым будет выполнен заказ
        auth = userClient.createNewUser(user).extract().body().path("accessToken");

        //Создание заказа
        ValidatableResponse orderResponse = orderClient.createNewOrderWithToken(order, auth);

        //Проверка результата создания заказа
        int statusCode = orderResponse.extract().statusCode();
        boolean status = orderResponse.extract().body().path("success");

        assertThat("Response status not 200", statusCode, equalTo(200));
        assertThat("Response body does not contain success status ", status, equalTo(true));
    }

    @Test
    @DisplayName("Non auth user create order")
    public void createOrderTestWithoutToken() {

        //Создание заказа
        ValidatableResponse orderResponse = orderClient.createNewOrderWithoutToken(order);

        //Проверка результата создания заказа
        int statusCode = orderResponse.extract().statusCode();
        boolean status = orderResponse.extract().body().path("success");

        assertThat("Response status not 200", statusCode, equalTo(200));
        assertThat("Response body does not contain success status ", status, equalTo(true));
    }

    @Test
    @DisplayName("Non auth user create order with broken hashId")
    public void createOrderTestWithNonCorrectHash() {

        //Создание заказа
        order.setBun("test");
        ValidatableResponse orderResponse = orderClient.createNewOrderWithoutToken(order);

        //Проверка результата создания заказа
        int statusCode = orderResponse.extract().statusCode();

        assertThat("Response status not 500", statusCode, equalTo(500));
    }

    @Test
    @DisplayName("Create order without ingredients")
    public void createOrderWithoutIngredients() {

        //
        ValidatableResponse orderResponse = orderClient.createNewOrderWithoutIngredients();

        //Проверка результата создания заказа
        int statusCode = orderResponse.extract().statusCode();
        String status = orderResponse.extract().body().path("message");

        assertThat("Response status not 400", statusCode, equalTo(400));
        assertThat("Response body does not contain success status ", status, equalTo("Ingredient ids must be provided"));
    }
}

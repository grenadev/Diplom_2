package com.ya.utils;

import com.ya.client.OrderClient;
import com.ya.client.UserClient;
import com.ya.model.Order;
import io.restassured.response.ResponseBodyExtractionOptions;
import io.restassured.response.ValidatableResponse;

import java.util.ArrayList;

import static org.hamcrest.Matchers.hasItems;


public class OrderDataGenerator{


    public static Order generateOrder() {
        OrderClient orderClient = new OrderClient();
        ValidatableResponse loginResponse = orderClient.getIngredients();

        ArrayList<String> bunList = loginResponse.extract().body().path("data.findAll { it.type == \"bun\"}._id");
        ArrayList<String> sauceList = loginResponse.extract().body().path("data.findAll { it.type == \"sauce\"}._id");
        ArrayList<String> mainList = loginResponse.extract().body().path("data.findAll { it.type == \"main\"}._id");

        int bunIndex = (int)(Math.random()*bunList.size());
        int sauceIndex = (int)(Math.random()*sauceList.size());
        int mainIndex = (int)(Math.random()*mainList.size());


        Order order = new Order(bunList.get(bunIndex),
                                sauceList.get(sauceIndex),
                                 mainList.get(mainIndex));

        return order;

    }

}

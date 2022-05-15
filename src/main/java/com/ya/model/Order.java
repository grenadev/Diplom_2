package com.ya.model;

public class Order {

    private String bun;
    private String souse;
    private String main;

    public Order (String bun, String souse, String main) {
        this.bun = bun;
        this.souse = souse;
        this.main = main;
    }

    public String getBun() {
        return bun;
    }

    public void setBun(String bun) {
        this.bun = bun;
    }

    public String getSouse() {
        return souse;
    }

    public void setSouse(String souse) {
        this.souse = souse;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }


}

package com.example.v4u.model;

public class Shop {
    String shop_id;
    String name;
    String image_url;
    double distance;

    public Shop() {

    }

    public Shop(String shop_id, String name, String image_url, double distance) {
        this.shop_id = shop_id;
        this.name = name;
        this.image_url = image_url;
        this.distance = distance;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package com.example.v4u.model;

import java.sql.Timestamp;
import java.util.List;

public class Order {
    String id;
    String status;
    String shop_name;
    List<Product> productList;
    Timestamp timestamp;

    public Order()
    {

    }

    public Order(String id, String status, String shop_name, List<Product> productList, Timestamp timestamp) {
        this.id = id;
        this.status = status;
        this.shop_name = shop_name;
        this.productList = productList;
        this.timestamp = timestamp;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}

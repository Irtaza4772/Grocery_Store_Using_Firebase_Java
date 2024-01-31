package com.example.assignment4;

public class Item {
    String name;
    String quantity;
    String timestamp;
    public Item(){

    }

    public Item(String name, String quantity, String timestamp) {
        this.name = name;
        this.quantity = quantity;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

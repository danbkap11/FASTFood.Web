package com.nure.backGardens.Models;

public class FoodInStorageModel {
    public int id;
    public double price;
    public double quantity;
    public String shelfLife;
    public FoodModel food;
    public String status;

    public FoodInStorageModel(int id, double price, double quantity, String shelfLife, String status) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.shelfLife = shelfLife;
        this.status = status;
    }
}

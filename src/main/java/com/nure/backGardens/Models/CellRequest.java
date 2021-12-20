package com.nure.backGardens.Models;

public class CellRequest {
    public int id;
    public String name;
    public String temp;
    public int storeId;
    public int foodId;
    public String shelfLife;
    public double price;
    public String foodName;
    public String foodDescription;
    public double weight;
    public String status;
    public int number;
    public int providerId;
    public double providerPrice;
    public String providerName;
    public int providerUserId;
    public boolean isPacked;
    public String providerLocation;

    public CellRequest(int id, String temp, int storeId, int foodId, String shelfLife, double price, String foodName, String foodDescription, double weight, String status, int number,
                       int providerId, double providerPrice, String providerName, int providerUserId, boolean isPacked, String providerLocation) {
        this.id = id;
        this.temp = temp;
        this.storeId = storeId;
        this.foodId = foodId;
        this.shelfLife = shelfLife;
        this.price = price;
        this.foodName = foodName;
        this.foodDescription = foodDescription;
        this.weight = weight;
        this.status = status;
        this.number = number;
        this.providerId = providerId;
        this.providerPrice = providerPrice;
        this.providerName = providerName;
        this.providerUserId = providerUserId;
        this.isPacked = isPacked;
        this.providerLocation = providerLocation;
    }
    public CellRequest(int id, int storeId, String status, int number) {
        this.id = id;
        this.storeId = storeId;
        this.status = status;
        this.number = number;
    }
    public void updCellRequest(String temp, int foodId, String shelfLife, double price, String foodName, String foodDescription,
                               double weight, int providerId, double providerPrice, String providerName, int providerUserId, boolean isPacked, String providerLocation){
        this.temp = temp;
        this.foodId = foodId;
        this.shelfLife = shelfLife;
        this.price = price;
        this.foodName = foodName;
        this.foodDescription = foodDescription;
        this.weight = weight;
        this.providerId = providerId;
        this.providerPrice = providerPrice;
        this.providerName = providerName;
        this.providerUserId = providerUserId;
        this.isPacked = isPacked;
        this.providerLocation = providerLocation;
    }

}

package com.nure.backGardens.Models;

import java.util.List;

public class StorageRequest {

    public int authorId;

    public int id;

    public String name;

    public String description;

    public String temp;

    public Double capacity;

    public int storeId;

    public int tempId;

    public List<FoodInStorageModel> foodInStorageModelList;

    public List<OrderModel> orderList;

    public StorageRequest(int id, String name, String description, String temp, Double capacity, int tempId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.temp = temp;
        this.capacity = capacity;
        this.tempId = tempId;
    }

}

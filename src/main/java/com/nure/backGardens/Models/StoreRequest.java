package com.nure.backGardens.Models;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public class StoreRequest {
    public int id;

    public String name;

    public String location;

    public int roleId;

    public List<CellRequest> cellList;

    public double price;

    public int userId;

    public List<StorageRequest> storageList;

    public List<OrderModel> orderList;

    public int providerId;

    public FoodModel food;

    public StoreRequest(int id, String name, String location, int roleId) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.roleId = roleId;
    }
}

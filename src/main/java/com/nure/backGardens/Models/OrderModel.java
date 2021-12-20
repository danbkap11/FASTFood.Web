package com.nure.backGardens.Models;

import com.nure.backGardens.entites.FoodEntity;
import com.nure.backGardens.entites.StorageEntity;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.List;

public class OrderModel {
    public int id;
    public String dateOfOrder;
    public String status;
    public String shelfLife;
    public int providerId;
    public int purchaserId;
    public int foodId;
    public int storageId;
    public double quantity;
    public double price;
    public String providerName;
    public String purchaserName;
    public String foodName;

    public OrderModel(int id, String dateOfOrder, String status, int providerId, int purchaserId, int foodId, int storageId, double quantity, double price,
                      String providerName, String purchaserName, String foodName) {
        this.id = id;
        this.dateOfOrder = dateOfOrder;
        this.status = status;
        this.providerId = providerId;
        this.purchaserId = purchaserId;
        this.foodId = foodId;
        this.storageId = storageId;
        this.quantity = quantity;
        this.price = price;
        this.providerName = providerName;
        this.purchaserName = purchaserName;
        this.foodName = foodName;
    }
}

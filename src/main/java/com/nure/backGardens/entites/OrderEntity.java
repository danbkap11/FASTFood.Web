package com.nure.backGardens.entites;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "order_table")
@Data
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id = -1;

    @Column
    private String dateOfOrder;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private StatusEntity statusEntity;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private StoreEntity provider;

    @ManyToOne
    @JoinColumn(name = "purchaser_id")
    private StoreEntity purchaser;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private FoodEntity foodEntity;

    @Column
    private String shelfLife;

    @ManyToOne
    @JoinColumn(name = "storage_id")
    private StorageEntity storageEntity;

    @Column
    private double quantity;

    @Column
    private double price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateOfOrder() {
        return dateOfOrder;
    }

    public void setDateOfOrder(String dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
    }

    public StatusEntity getStatusEntity() {
        return statusEntity;
    }

    public void setStatusEntity(StatusEntity statusEntity) {
        this.statusEntity = statusEntity;
    }

    public StoreEntity getProvider() {
        return provider;
    }

    public void setProvider(StoreEntity provider) {
        this.provider = provider;
    }

    public StoreEntity getPurchaser() {
        return purchaser;
    }

    public void setPurchaser(StoreEntity purchaser) {
        this.purchaser = purchaser;
    }

    public FoodEntity getFoodEntity() {
        return foodEntity;
    }

    public void setFoodEntity(FoodEntity foodEntity) {
        this.foodEntity = foodEntity;
    }

    public String getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(String shelfLife) {
        this.shelfLife = shelfLife;
    }

    public StorageEntity getStorageEntity() {
        return storageEntity;
    }

    public void setStorageEntity(StorageEntity storageEntity) {
        this.storageEntity = storageEntity;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
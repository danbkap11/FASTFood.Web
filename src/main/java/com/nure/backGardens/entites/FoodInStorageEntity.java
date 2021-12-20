package com.nure.backGardens.entites;

import com.nure.backGardens.Models.PutFoodRequest;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "foodInStorage_table")
@Data
public class FoodInStorageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id = -1;

    @ManyToOne
    @JoinColumn(name = "storage_id")
    private StorageEntity storageEntity;

    @Column
    private double quantity;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private FoodEntity foodEntity;

    @Column
    private String shelfLife;

    @Column
    private Double price;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private StatusEntity statusId;


    @Column
    private Double weight = 0.0d;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public StatusEntity getStatusId() {
        return statusId;
    }

    public void setStatusId(StatusEntity statusId) {
        this.statusId = statusId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public void setData(PutFoodRequest pfr, StorageEntity se, FoodEntity fe){
        if(se != null){
            storageEntity = se;
        }
        if(pfr.quantity != 0){
            quantity = pfr.quantity;
        }
        if(fe != null){
            foodEntity = fe;
        }
        if(pfr.price != 0){
            price = pfr.price;
        }
        if(pfr.weight != 0.0d && pfr.weight > 0){
            this.weight = pfr.weight;
        }
    }
}

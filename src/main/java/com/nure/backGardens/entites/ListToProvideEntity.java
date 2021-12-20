package com.nure.backGardens.entites;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "listToProvide_table")
@Data
public class ListToProvideEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id = -1;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private StoreEntity storeEntity;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private FoodEntity foodEntity;

    @Column
    private double price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StoreEntity getStoreEntity() {
        return storeEntity;
    }

    public void setStoreEntity(StoreEntity storeEntity) {
        this.storeEntity = storeEntity;
    }

    public FoodEntity getFoodEntity() {
        return foodEntity;
    }

    public void setFoodEntity(FoodEntity foodEntity) {
        this.foodEntity = foodEntity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


}

package com.nure.backGardens.entites;

import com.nure.backGardens.Models.AutoOrderRequest;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "autoOrder_table")
@Data
public class AutoOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id = -1;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private FoodEntity foodEntity;

    @ManyToOne
    @JoinColumn(name = "storage_id")
    private StorageEntity storageEntity;

    @Column
    private double quantityInStorage;

    @Column
    private double quantityToOrder;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private StoreEntity provider;

    @Column
    private double price;

    @ManyToOne
    @JoinColumn(name = "cell_id")
    private CellEntity cellEntity;

    public AutoOrderEntity(FoodEntity foodEntity, double quantityInStorage, double quantityToOrder, StoreEntity provider, double price) {
        this.foodEntity = foodEntity;
        this.quantityInStorage = quantityInStorage;
        this.quantityToOrder = quantityToOrder;
        this.provider = provider;
        this.price = price;
    }

    public AutoOrderEntity() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFoodEntity(FoodEntity foodEntity) {
        this.foodEntity = foodEntity;
    }

    public StorageEntity getStorageEntity() {
        return storageEntity;
    }

    public void setStorageEntity(StorageEntity storageEntity) {
        this.storageEntity = storageEntity;
    }

    public double getQuantityInStorage() {
        return quantityInStorage;
    }

    public void setQuantityInStorage(double quantityInStorage) {
        this.quantityInStorage = quantityInStorage;
    }

    public double getQuantityToOrder() {
        return quantityToOrder;
    }

    public void setQuantityToOrder(double quantityToOrder) {
        this.quantityToOrder = quantityToOrder;
    }

    public FoodEntity getFoodEntity() {
        return foodEntity;
    }

    public StoreEntity getProvider() {
        return provider;
    }

    public void setProvider(StoreEntity providerStorage) {
        this.provider = providerStorage;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public CellEntity getCellEntity() {
        return cellEntity;
    }

    public void setCellEntity(CellEntity cellEntity) {
        this.cellEntity = cellEntity;
    }

    public void updateAutoOrder(AutoOrderRequest autoOrderRequest, StoreEntity provider){
        this.quantityInStorage = autoOrderRequest.quantityInStorage;
        this.quantityToOrder = autoOrderRequest.quantityToOrder;
        this.provider = provider;
        this.price = autoOrderRequest.price;
    }
}
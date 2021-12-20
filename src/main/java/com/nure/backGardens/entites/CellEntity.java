package com.nure.backGardens.entites;

import com.nure.backGardens.Models.CellRequest;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "Cell_table")
@Data
public class CellEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id = -1;

    @Column
    private int number;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private StoreEntity storeEntity;

    @ManyToOne
    @JoinColumn(name = "foodOfProvider_id")
    private ListToProvideEntity listToProvideEntity;

    private String shelfLife;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private StatusEntity statusEntity;

    @Column
    private double price;

    @Column
    private double weight;

    public CellEntity(int number, StoreEntity storeEntity, ListToProvideEntity listToProvideEntity, double price, double weight) {
        this.id = id;
        this.number = number;
        this.storeEntity = storeEntity;
        this.listToProvideEntity = listToProvideEntity;
        this.statusEntity = statusEntity;
        this.price = price;
        this.weight = weight;
    }


    public CellEntity(int number, StoreEntity storeEntity) {
        this.number = number;
        this.storeEntity = storeEntity;
    }

    public CellEntity() {
    }

    public void updateCell(CellRequest cellRequest, ListToProvideEntity listToProvideEntity){
        this.listToProvideEntity = listToProvideEntity;
        if(cellRequest.price != 0.0d){
            this.price = cellRequest.price;
        }
        if(cellRequest.weight != 0.0d){
            this.weight = cellRequest.weight;
        }
        this.shelfLife = cellRequest.shelfLife;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public StoreEntity getStoreEntity() {
        return storeEntity;
    }

    public void setStoreEntity(StoreEntity storeEntity) {
        this.storeEntity = storeEntity;
    }

    public ListToProvideEntity getListToProvideEntity() {
        return listToProvideEntity;
    }

    public void setListToProvideEntity(ListToProvideEntity listToProvideEntity) {
        this.listToProvideEntity = listToProvideEntity;
    }

    public String getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(String shelfLife) {
        this.shelfLife = shelfLife;
    }

    public StatusEntity getStatusEntity() {
        return statusEntity;
    }

    public void setStatusEntity(StatusEntity statusEntity) {
        this.statusEntity = statusEntity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
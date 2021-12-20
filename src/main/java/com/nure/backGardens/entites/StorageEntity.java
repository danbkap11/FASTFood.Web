package com.nure.backGardens.entites;

import com.nure.backGardens.Models.StorageRequest;
import com.nure.backGardens.Models.UserRequest;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "storage_table")
@Data
public class StorageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id = -1;

    @Column
    private String name;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "tmp_table")
    private TmpEntity tempEntity;

    @Column
    private Double capacity;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private StoreEntity storeEntity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TmpEntity getTmpEntity() {
        return tempEntity;
    }

    public void setTmpEntity(TmpEntity tempEntity) {
        this.tempEntity = tempEntity;
    }

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public StoreEntity getStoreEntity() {
        return storeEntity;
    }

    public void setStoreEntity(StoreEntity storeEntity) {
        this.storeEntity = storeEntity;
    }

    public void setFields(StorageRequest sr, TmpEntity tempEntity){
        if(sr.name != null){
            this.name = sr.name;
        }
        if(sr.description != null){
            this.description = sr.description;
        }
        if(sr.temp != null){
            this.tempEntity = tempEntity;
        }
    }

    public TmpEntity getTempEntity() {
        return tempEntity;
    }

    public void setTempEntity(TmpEntity tempEntity) {
        this.tempEntity = tempEntity;
    }

}
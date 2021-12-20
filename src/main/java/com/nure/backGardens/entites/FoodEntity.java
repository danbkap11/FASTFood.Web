package com.nure.backGardens.entites;

import com.nure.backGardens.Models.FoodModel;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "food_table")
@Data
public class FoodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id = -1;

    @Column
    private String name;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "tmp_id")
    private TmpEntity tempEntity;

    @Column
    private double occupiedSpace;

    @Column
    private boolean isPacked;

    public boolean isPacked() {
        return isPacked;
    }

    public void setPacked(boolean packed) {
        isPacked = packed;
    }

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

    public TmpEntity getTempEntity() {
        return tempEntity;
    }

    public void setTempEntity(TmpEntity tempEntity) {
        this.tempEntity = tempEntity;
    }

    public double getOccupiedSpace() {
        return occupiedSpace;
    }

    public void setOccupiedSpace(double occupiedSpace) {
        this.occupiedSpace = occupiedSpace;
    }


    public void setFields(FoodModel fr, TmpEntity tempEntity){
        if(fr.name != null){
            this.name = fr.name;
        }
        if(fr.description != null){
            this.description = fr.description;
        }
        if(tempEntity != null){
            this.tempEntity = tempEntity;
        }
        if(fr.occupiedSpace != 0.0d && fr.occupiedSpace > 0){
            this.occupiedSpace = fr.occupiedSpace;
        }
        this.isPacked = fr.isPacked;
    }

}

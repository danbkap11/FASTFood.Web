package com.nure.backGardens.entites;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "tmp_table")
@Data
public class TmpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id = -1;

    @Column(unique=true)
    private String name;

    @Column
    private  int minTmp;

    @Column
    private  int maxTmp;


    public int getMinTmp() {
        return minTmp;
    }

    public void setMinTmp(int minTmp) {
        this.minTmp = minTmp;
    }

    public int getMaxTmp() {
        return maxTmp;
    }

    public void setMaxTmp(int maxTmp) {
        this.maxTmp = maxTmp;
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

    public String getDesc(){
        return name + " ( " + minTmp + " - " + maxTmp + ")";
    }



}

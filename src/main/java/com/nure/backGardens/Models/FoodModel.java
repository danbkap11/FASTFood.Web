package com.nure.backGardens.Models;

public class FoodModel {
    public int updatedById;
    public int providerId;
    public int id;
    public String name;
    public String description;
    public String temp;
    public int tempId;
    public double occupiedSpace;
    public boolean isPacked;
    public double weight;

    public FoodModel(int id, String name, String description, String temp, double occupiedSpace, boolean isPacked) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.temp = temp;
        this.occupiedSpace = occupiedSpace;
        this.isPacked = isPacked;
    }

    public FoodModel(int id, String name, String description, boolean isPacked, double occupiedSpace) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.occupiedSpace = occupiedSpace;
        this.isPacked = isPacked;
    }
}

package com.nure.backGardens.Models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;


public class PutFoodRequest {
    public int id;
    public int foodId;
    public int storageId;
    public double quantity;
    public String shelfLife;
    public double price;
    public double weight;

}

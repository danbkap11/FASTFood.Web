package com.nure.backGardens.service;

import com.google.gson.JsonObject;
import com.nure.backGardens.Models.*;
import lombok.SneakyThrows;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {
    public JsonObject json = new JsonObject();


    @SneakyThrows
    public JsonObject returnUser(Boolean status, String returnInfo, UserRequest userRequest, String modelName) {
        json = new JsonObject();
        this.json.addProperty("returnInfo", returnInfo);
        json.addProperty("status", status);
        if(status){
            ObjectMapper om = new ObjectMapper();
            this.json.addProperty(modelName, om.writeValueAsString(userRequest));
        }
        return this.json;
    }

    @SneakyThrows
    public JsonObject returnInfo(Boolean status, String returnInfo) {
        json = new JsonObject();
        json.addProperty("returnInfo", returnInfo);
        json.addProperty("status", status);
        return json;
    }

    @SneakyThrows
    public JsonObject returnStoreList(Boolean status, String returnInfo, List<StoreRequest> storeRequestList, String modelName) {
        json = new JsonObject();
        this.json.addProperty("returnInfo", returnInfo);
        json.addProperty("status", status);
        if(status){
            ObjectMapper om = new ObjectMapper();
            this.json.addProperty(modelName, om.writeValueAsString(storeRequestList));
        }
        return this.json;
    }

    @SneakyThrows
    public JsonObject returnCell(Boolean status, String returnInfo, CellRequest cellRequest, String modelName) {
        json = new JsonObject();
        json.addProperty("returnInfo", returnInfo);
        json.addProperty("status", status);
        if(status){
            ObjectMapper om = new ObjectMapper();
            this.json.addProperty(modelName, om.writeValueAsString(cellRequest));
        }
        return json;
    }

    @SneakyThrows
    public JsonObject returnFoodList(Boolean status, String returnInfo, List<FoodModel> foodModelList, String modelName) {
        json = new JsonObject();
        json.addProperty("returnInfo", returnInfo);
        json.addProperty("status", status);
        if(status){
            ObjectMapper om = new ObjectMapper();
            this.json.addProperty(modelName, om.writeValueAsString(foodModelList));
        }
        return json;
    }

    @SneakyThrows
    public JsonObject returnProvidersList(Boolean status, String returnInfo, List<StoreRequest> storeRequestList, String modelName) {
        json = new JsonObject();
        json.addProperty("returnInfo", returnInfo);
        json.addProperty("status", status);
        if(status){
            ObjectMapper om = new ObjectMapper();

            this.json.addProperty(modelName, om.writeValueAsString(storeRequestList));
        }
        return json;
    }

    @SneakyThrows
    public JsonObject returnStore(Boolean status, String returnInfo, StoreRequest storeRequest, String modelName) {
        json = new JsonObject();
        json.addProperty("returnInfo", returnInfo);
        json.addProperty("status", status);
        if(status){
            ObjectMapper om = new ObjectMapper();
            this.json.addProperty(modelName, om.writeValueAsString(storeRequest));
        }
        return json;
    }
    @SneakyThrows
    public JsonObject returnStorageList(Boolean status, String returnInfo, List<StorageRequest> storageRequestList, String modelName) {
        json = new JsonObject();
        json.addProperty("returnInfo", returnInfo);
        json.addProperty("status", status);
        if(status){
            ObjectMapper om = new ObjectMapper();
            this.json.addProperty(modelName, om.writeValueAsString(storageRequestList));
        }
        return json;
    }

    @SneakyThrows
    public JsonObject returnOrderModelList(Boolean status, String returnInfo, List<OrderModel> orderModelList, String modelName) {
        json = new JsonObject();
        json.addProperty("returnInfo", returnInfo);
        json.addProperty("status", status);
        if(status){
            ObjectMapper om = new ObjectMapper();
            this.json.addProperty(modelName, om.writeValueAsString(orderModelList));
        }
        return json;
    }
}
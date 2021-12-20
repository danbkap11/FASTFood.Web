package com.nure.backGardens.service;


import com.nure.backGardens.entites.FoodEntity;
import com.nure.backGardens.entites.StorageEntity;
import com.nure.backGardens.entites.StoreEntity;
import com.nure.backGardens.repository.FoodEntityRepository;
import com.nure.backGardens.repository.StorageEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodService {

    @Autowired
    private FoodEntityRepository foodEntityRepository;
    @Autowired
    private StorageEntityRepository storageEntityRepository;

    public FoodEntity save(FoodEntity foodEntity) {
        return foodEntityRepository.save(foodEntity);
    }
    public StorageEntity updateStorage(StorageEntity storageEntity) {
        return storageEntityRepository.save(storageEntity);
    }
    public FoodEntity findById(int id) {
        return foodEntityRepository.findById(id);
    }
    public  void  delete(FoodEntity foodEntity){
        foodEntityRepository.delete(foodEntity);
    }

    public List<FoodEntity> getFoodList(){
        return foodEntityRepository.findBy();
    }

}

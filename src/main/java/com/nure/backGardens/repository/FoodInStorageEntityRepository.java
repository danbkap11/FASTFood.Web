package com.nure.backGardens.repository;

import com.nure.backGardens.entites.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodInStorageEntityRepository extends JpaRepository<FoodInStorageEntity, Integer> {

    List<FoodInStorageEntity> findByStorageEntity(StorageEntity se);
    FoodInStorageEntity findById(int id);
    List<FoodInStorageEntity> findByStorageEntityAndFoodEntityAndShelfLife(StorageEntity se, FoodEntity fe, String sl);
    void deleteById(int id);
}
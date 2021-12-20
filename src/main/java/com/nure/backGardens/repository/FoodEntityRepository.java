package com.nure.backGardens.repository;


import com.nure.backGardens.entites.FoodEntity;
import com.nure.backGardens.entites.FoodInStorageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodEntityRepository extends JpaRepository<FoodEntity, Integer> {

    FoodEntity findByName(String name);
    FoodEntity findById(int id);

    List<FoodEntity> findBy();

    void deleteById(int aLong);
}


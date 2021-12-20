package com.nure.backGardens.repository;

import com.nure.backGardens.entites.AutoOrderEntity;
import com.nure.backGardens.entites.FoodEntity;
import com.nure.backGardens.entites.StorageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AutoOrderEntityRepository extends JpaRepository<AutoOrderEntity, Integer> {

    AutoOrderEntity findById(int id);

    List<AutoOrderEntity> findByStorageEntity(StorageEntity storageEntity);

    void deleteById(int id);
}


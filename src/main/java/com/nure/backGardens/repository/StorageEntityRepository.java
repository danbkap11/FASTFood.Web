package com.nure.backGardens.repository;

import com.nure.backGardens.entites.StorageEntity;
import com.nure.backGardens.entites.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface StorageEntityRepository extends JpaRepository<StorageEntity, Integer> {
    StorageEntity findById(int id);
    void deleteById(int id);
    List<StorageEntity> findByStoreEntity(StoreEntity storeEntity);
}

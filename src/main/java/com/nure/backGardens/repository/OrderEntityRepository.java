package com.nure.backGardens.repository;

import com.nure.backGardens.entites.FoodEntity;
import com.nure.backGardens.entites.OrderEntity;
import com.nure.backGardens.entites.StorageEntity;
import com.nure.backGardens.entites.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderEntityRepository extends JpaRepository<OrderEntity, Integer> {

    List<OrderEntity> findByPurchaser(StoreEntity se);
    OrderEntity findById(int id);
    List<OrderEntity> findByStorageEntity(StorageEntity storageEntity);

    List<OrderEntity> findByProvider(StoreEntity storeEntity);

    void deleteById(int aLong);
}
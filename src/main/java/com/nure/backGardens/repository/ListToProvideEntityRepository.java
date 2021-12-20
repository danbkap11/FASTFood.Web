package com.nure.backGardens.repository;

import com.nure.backGardens.entites.FoodEntity;
import com.nure.backGardens.entites.ListToProvideEntity;
import com.nure.backGardens.entites.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListToProvideEntityRepository extends JpaRepository<ListToProvideEntity, Integer> {

    ListToProvideEntity findById(int id);

    List<ListToProvideEntity> findByFoodEntity(FoodEntity foodEntity);

    List<ListToProvideEntity> findByStoreEntity(StoreEntity storeEntity);

    List<ListToProvideEntity> findByStoreEntityAndFoodEntity(StoreEntity storeEntity, FoodEntity foodEntity);

    void deleteById(int aLong);
}
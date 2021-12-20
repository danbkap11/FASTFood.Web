package com.nure.backGardens.repository;

import com.nure.backGardens.entites.CellEntity;
import com.nure.backGardens.entites.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CellEntityRepository extends JpaRepository<CellEntity, Integer> {

    CellEntity findById(int id);
    List<CellEntity> findByStoreEntity(StoreEntity storeEntity);
    CellEntity findByStoreEntityAndNumber(StoreEntity storeEntity, int number);

    void deleteById(int id);
}
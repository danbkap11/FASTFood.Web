package com.nure.backGardens.repository;


import com.nure.backGardens.entites.TmpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TmpEntityRepository extends JpaRepository<TmpEntity, Integer> {

    TmpEntity findByName(String name);
    TmpEntity findById(int id);
}

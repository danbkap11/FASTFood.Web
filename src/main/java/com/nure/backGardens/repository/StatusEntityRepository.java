package com.nure.backGardens.repository;

import com.nure.backGardens.entites.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusEntityRepository extends JpaRepository<StatusEntity, Integer> {

    StatusEntity findByName(String name);
}

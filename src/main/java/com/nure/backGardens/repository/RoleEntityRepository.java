package com.nure.backGardens.repository;


import com.nure.backGardens.entites.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleEntityRepository extends JpaRepository<RoleEntity, Integer> {

    RoleEntity findByName(String name);
    RoleEntity findById(int id);
}

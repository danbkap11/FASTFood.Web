package com.nure.backGardens.repository;


import com.nure.backGardens.entites.RoleEntity;
import com.nure.backGardens.entites.StoreEntity;
import com.nure.backGardens.entites.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreEntityRepository extends JpaRepository<StoreEntity, Integer> {
    StoreEntity findById(int id);
    void deleteById(int id);
    StoreEntity findByUserEntity(UserEntity user);
    List<StoreEntity> findByUserEntityAndRoleEntity(UserEntity user, RoleEntity roleEntity);
    List<StoreEntity> findByRoleEntity(RoleEntity roleEntity);
    List<StoreEntity> findByRoleEntityAndUserEntityIsNot(RoleEntity roleEntity, UserEntity user);
}

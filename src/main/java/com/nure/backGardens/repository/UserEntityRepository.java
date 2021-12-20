package com.nure.backGardens.repository;


import com.nure.backGardens.entites.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntityRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity findByLogin(String login);
    UserEntity findById(int id);
    UserEntity findByEmail(String email);
    void deleteById(int id);
}

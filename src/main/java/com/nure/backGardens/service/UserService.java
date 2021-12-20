package com.nure.backGardens.service;


import com.nure.backGardens.entites.RoleEntity;
import com.nure.backGardens.entites.UserEntity;
import com.nure.backGardens.repository.RoleEntityRepository;
import com.nure.backGardens.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserEntityRepository userEntityRepository;
    @Autowired
    private RoleEntityRepository roleEntityRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserEntity createUser(UserEntity userEntity) {
        RoleEntity userRole = roleEntityRepository.findByName("ROLE_USER");
        userEntity.setRoleEntity(userRole);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userEntityRepository.save(userEntity);
    }

    public UserEntity changeUser(UserEntity userEntity) {
        return userEntityRepository.save(userEntity);
    }

    public UserEntity changePassword(UserEntity userEntity, String password, String oldPassword) {
        if(passwordEncoder.matches(oldPassword, userEntity.getPassword())){
            userEntity.setPassword(passwordEncoder.encode(password));
            return userEntityRepository.save(userEntity);
        } else {
            return null;
        }
    }

    public UserEntity changePasswordByAdmin(UserEntity userEntity, String password) {
        userEntity.setPassword(passwordEncoder.encode(password));
        return userEntityRepository.save(userEntity);
    }


    public UserEntity findByLogin(String login) {
        return userEntityRepository.findByLogin(login);
    }
    public UserEntity findByEmail(String email) {
        return userEntityRepository.findByEmail(email);
    }

    public UserEntity findById(int id) {
        return userEntityRepository.findById(id);
    }
    public void delById(Integer id) {
        userEntityRepository.deleteById(id);
    }

    public UserEntity findByLoginAndPassword(String login, String password) {
        UserEntity userEntity = findByLogin(login);
        if (userEntity != null) {
            if (passwordEncoder.matches(password, userEntity.getPassword())) {
                return userEntity;
            }
        }
        return null;
    }
}

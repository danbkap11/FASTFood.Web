package com.nure.backGardens.Models;

import com.nure.backGardens.entites.UserEntity;
import com.nure.backGardens.service.UserService;
import com.nure.backGardens.service.ValidateService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotEmpty;


public class UserRequest {

    public  int id;

    public String login;

    public String name;

    public String password;

    public String oldPassword;

    public String email;

    public String phone;

    public UserRequest(int id, String login, String name, String email, String phone) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
}

package com.nure.backGardens.entites;


import com.nure.backGardens.Models.UserRequest;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Map;


@Entity
@Table(name = "user_table")
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id = -1;

    @Column(unique=true)
    private String login;

    @Column
    private String name;

    @Column
    private String password;

    @Column(unique=true)
    private String email;

    @Column
    private String phone;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity roleEntity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public RoleEntity getRoleEntity() {
        return roleEntity;
    }

    public void setRoleEntity(RoleEntity roleEntity) {
        this.roleEntity = roleEntity;
    }

    public void setReqFields(UserRequest ur){
        if(ur.password != null){
            this.password = ur.password;
        }
        if(ur.login != null){
            this.login = ur.login;
        }
        if(ur.name != null){
            this.name = ur.name;
        }
        if(ur.email != null){
            this.email = ur.email;
        }
        if(ur.phone != null){
            this.phone = ur.phone;
        }
    }
}

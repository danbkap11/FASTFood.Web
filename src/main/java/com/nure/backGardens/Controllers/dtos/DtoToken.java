package com.nure.backGardens.Controllers.dtos;

public class DtoToken {


        private String token;

    public DtoToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

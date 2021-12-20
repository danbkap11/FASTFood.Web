package com.nure.backGardens.Controllers;

import com.google.gson.JsonObject;
import com.nure.backGardens.Controllers.dtos.*;
import com.nure.backGardens.Models.StorageRequest;
import com.nure.backGardens.Models.UserRequest;
import com.nure.backGardens.config.jwt.JwtProvider;
import com.nure.backGardens.entites.StoreEntity;
import com.nure.backGardens.entites.UserEntity;
import com.nure.backGardens.service.RequestService;
import com.nure.backGardens.service.UserService;

import com.nure.backGardens.service.ValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = {"*"} )
@RestController
public class UserController {

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private RequestService requestService;

    @GetMapping("/da")
    public String da() {
        return "OK";
    }

    @PostMapping("/registerUser")
    public JsonObject registerUser(@RequestBody @Valid UserRequest req) {
        String returnInfo = ValidateService.validateUser("registration",req, userService, "");
        JsonObject json = new JsonObject();
        if(returnInfo.equals("OK")){
            UserEntity user = new UserEntity();
            user.setReqFields(req);
            userService.createUser(user);
            String token = jwtProvider.generateToken(user.getLogin());
            json.addProperty("returnInfo", "USER REGISTERED");
            json.addProperty("status", true);
            json.addProperty("token", token);

            json.addProperty("isAdmin", false);
            return json;
        }
        json.addProperty("returnInfo", returnInfo);
        json.addProperty("status", false);
        return json;
    }

    @PostMapping("/changeUserInfo")
    public JsonObject changeUserInfo(HttpServletRequest request, @RequestBody @Valid UserRequest req) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        if(user == null){
            return requestService.returnInfo(false,"INVALID USER");
        }
        if( user.getRoleEntity().getId() == 1){

        } else {
            String returnInfo = ValidateService.validateUser("changeUserInfo", req, userService, user.getEmail());
            if(returnInfo.equals("OK")) {
                user.setReqFields(req);
                userService.changeUser(user);
                UserRequest userRequest = new UserRequest(user.getId(), user.getLogin(), user.getName(), user.getEmail(), user.getPhone());
                return requestService.returnUser(true, "USER INFO UPDATED", userRequest, "user");
            }
            return requestService.returnInfo(false,returnInfo);
        }
        return requestService.returnInfo(false,"SERVER ERROR");
    }

    @PostMapping("/changePassword")
    public JsonObject changePassword(HttpServletRequest request, @RequestBody @Valid UserRequest req) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        if(user == null){
            return requestService.returnInfo(false,"INVALID USER");
        }
        if(user.getRoleEntity().getId() == 1) {
            UserEntity userToChange = userService.findByLogin(req.login);
            userService.changePasswordByAdmin(userToChange, req.password);
            return requestService.returnInfo(true, "PASSWORD UPDATED");
        } else  {
            if(req.password != null || req.password.length() <= 5) {

                if(userService.changePassword(user, req.password, req.oldPassword) == null){
                    return requestService.returnInfo(false,"INVALID PASSWORD");
                } else {
                    return requestService.returnInfo(true, "PASSWORD UPDATED");
                }
            }
            return requestService.returnInfo(false,"INVALID PASSWORD FORMAT");
        }
    }

    @PostMapping("/login")
    public JsonObject auth(@RequestBody UserRequest request) {
        UserEntity userEntity = userService.findByLoginAndPassword(request.login, request.password);
        JsonObject json = new JsonObject();
        if(userEntity == null){
            json.addProperty("status", false);
            json.addProperty("returnInfo", "INVALID CREDENTIALS");
            return json;
        }
        String token = jwtProvider.generateToken(userEntity.getLogin());
        json.addProperty("status", true);
        json.addProperty("returnInfo", "Login Successful");
        json.addProperty("token", token);
        if(userEntity.getRoleEntity().getId() == 1){
            json.addProperty("isAdmin", true);
        }
        if(userEntity.getRoleEntity().getId() == 2){
            json.addProperty("isAdmin", false);
        }

        return json;
    }

    @GetMapping("/getUser")
    public JsonObject getMachines(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        if(user == null){
            return requestService.returnUser(false, "INVALID USER", null, null);
        }
        UserRequest userRequest = new UserRequest(user.getId(), user.getLogin(), user.getName(), user.getEmail(), user.getPhone());
        return requestService.returnUser(true, "", userRequest, "user");
    }

    @GetMapping("/getUserRole")
    public JsonObject isUserValid(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));

        if(user == null){
            return requestService.returnInfo(false, "INVALID USER");
        }
        return requestService.returnInfo(true, user.getRoleEntity().getName());
    }
}

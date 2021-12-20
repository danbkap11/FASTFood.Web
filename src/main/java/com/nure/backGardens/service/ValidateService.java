package com.nure.backGardens.service;

import com.nure.backGardens.Models.*;
import com.nure.backGardens.entites.*;
import org.springframework.stereotype.Service;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

@Service
public class ValidateService {

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    public static String validateUser(String sit, UserRequest req, UserService us, String email){

        if(sit.equals("registration") &&(req.login == null || req.login.length() < 3)){
            return "INVALID LOGIN";
        }
        UserEntity userEntity = us.findByLogin(req.login);
        if(userEntity != null){
            return "THIS LOGIN IS REQUIRED";
        }
        if(sit.equals("registration") && (req.password == null || req.password.length() < 4)){
            return "INVALID PASSWORD";
        }
        if(req.name == null || req.name.length() < 2){
            return "INVALID NAME";
        }
        if(req.phone == null || String.valueOf(req.phone).length() != 10){
            return "INVALID PHONE";
        }
        if(!isValidEmailAddress(req.email)){
            return "INVALID EMAIL";
        }
        userEntity = us.findByEmail(req.email);
        if(userEntity != null && !email.equals(userEntity.getEmail())){
            return "THIS EMAIL IS REQUIRED";
        }
        return "OK";
    }

    public static boolean validateStore(StoreRequest sr){
        if(sr != null && sr != null){
            return true;
        } else {
            return false;
        }
    }

    public static String validateStorage(StorageRequest sr){
        String ret = "INVALID DATA FORMAT";
        if(sr.name == null || sr.name.length() < 4){
            return  ret;
        }
        if(sr.capacity == null){
            return  ret;
        }
        return "OK";
    }

    public static boolean validateFood(FoodModel fr, TmpEntity tmp){
        if(fr.name == null || fr.name.length() < 4){
            System.out.println(fr.name);
            return  false;
        }
        if(tmp == null){
            System.out.println(tmp.getName());
            return  false;
        }
        if(fr.occupiedSpace == 0.0d || fr.occupiedSpace < 0){
            System.out.println(fr.occupiedSpace);
            return  false;
        }
        return true;
    }

    public static String validatePutFood(PutFoodRequest pfr, FoodEntity fe, StorageEntity st, double freeSpace) {
        if(!st.getTempEntity().equals(fe.getTempEntity())){
            return "INVALID TEMP MODE";
        }
        if(pfr.quantity <= 0){
            return "INVALID QUANTITY";
        }

        if(pfr.quantity * fe.getOccupiedSpace() > freeSpace){
            return "INVALID SPACE";
        }
        if(pfr.price < 0){
            return "INVALID PRICE";
        }
        return "OK";
    }
    public static String validateCell(CellRequest cellRequest){

        if(cellRequest.price <= 0){
            return "INVALID PRICE";
        }
        if(cellRequest.weight <= 0){
            return "INVALID WEIGHT";
        }
        return "VALID";
    }
}

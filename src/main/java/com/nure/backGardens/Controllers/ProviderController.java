package com.nure.backGardens.Controllers;

import com.google.gson.JsonObject;
import com.nure.backGardens.Models.*;
import com.nure.backGardens.config.jwt.JwtProvider;
import com.nure.backGardens.entites.*;
import com.nure.backGardens.service.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = {"*"} )
@RestController
public class ProviderController {

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private FoodService foodService;
    @Autowired
    private TmpService tmpService;
    @Autowired
    private RequestService requestService;

    @PostMapping("/registerProvider")
    public JsonObject registerProvider(HttpServletRequest request, @RequestBody @Valid StoreRequest req) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        if(user.getRoleEntity().getId() == 2 || user.getRoleEntity().getId() == 1) {
            if(ValidateService.validateStore(req)){
                StoreEntity st = new StoreEntity();
                st.setName(req.name);
                st.setLocation(req.location);
                st.setUserEntity(user);
                storeService.createProvider(st);
                return requestService.returnStoreList(true, "WAREHOUSE CREATED", findStoreListByUserForProvider(user), "shops");
            }
            return requestService.returnInfo(false, "INVALID DATA FORMAT");
        }
        return requestService.returnInfo(false, "INVALID USER");
    }

    @PostMapping("/updateProvider")
    public JsonObject changeStoreInfo(HttpServletRequest request, @RequestBody @Valid StoreRequest req) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        StoreEntity st = storeService.findById(req.id);
        if(user != null && user.getRoleEntity().getId() == 1){

        } else {
            if( !st.getUserEntity().equals(user) ) {
                return requestService.returnInfo(false, "INVALID USER");
            }
            if(st == null){
                return requestService.returnInfo(false, "INVALID PROVIDER");
            }
            if(ValidateService.validateStore(req)) {
                st.setName(req.name);
                st.setLocation(req.location);
                storeService.updateStore(st);
                return requestService.returnStoreList(true, "WAREHOUSE UPDATED", findStoreListByUserForProvider(user), "shops");
            }
            return requestService.returnInfo(false, "INVALID DATA FORMAT");
        }
        return requestService.returnInfo(false, "INVALID USER");
    }

    @PostMapping("/createFood")
    public JsonObject createFood(HttpServletRequest request, @RequestBody @Valid FoodModel req) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        if(user == null){
            return requestService.returnInfo(false, "INVALID USER");
        }
        if(user.getRoleEntity().getId() == 2){

        } else {
            TmpEntity tmp = tmpService.getById(req.tempId);
            if(ValidateService.validateFood(req, tmp)) {
                FoodEntity fe = new FoodEntity();
                fe.setFields(req, tmp);
                foodService.save(fe);
                List<FoodEntity>foodEntityList = foodService.getFoodList();
                List<FoodModel> foodModelList = new ArrayList<>();
                for(FoodEntity foodEntity: foodEntityList){
                    FoodModel foodModel = new FoodModel(foodEntity.getId(), foodEntity.getName(), foodEntity.getDescription(),
                            foodEntity.getTempEntity().getDesc(), foodEntity.getOccupiedSpace(), foodEntity.isPacked());
                    foodModelList.add(foodModel);
                }
                return requestService.returnFoodList(true, "FOOD CREATED", foodModelList, "foodList");
            }
            return requestService.returnInfo(false, "INVALID DATA FORMAT");
        }
        return requestService.returnInfo(false, "INVALID USER");
    }

    @SneakyThrows
    @PostMapping("/putFoodToStorage")
    public JsonObject putFoodToStorage(HttpServletRequest request, @RequestBody @Valid PutFoodRequest req) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        StorageEntity st = storageService.findById(req.storageId);
        FoodEntity fe = foodService.findById(req.foodId);
        if(user == null){
            return requestService.returnInfo(false, "INVALID USER");
        }
        if(user.getRoleEntity().getId() == 1){
        } else {

            if(st == null || !st.getStoreEntity().getUserEntity().equals(user)) {
                return requestService.returnInfo(false, "INVALID STORAGE");
            }
            if(fe == null) {
                return requestService.returnInfo(false, "INVALID FOOD");
            }
            String ret = ValidateService.validatePutFood(req, fe, st,
                    storageService.getFreeSpace(storageService.findFoodInStorageByStorageEntity(st), st, orderService.findByPurchaser(st.getStoreEntity()), orderService.findAutoOrderByStorage(st)));
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            if(req.shelfLife == null || sdf.parse(req.shelfLife).before(date)){
                return requestService.returnInfo(false, "INVALID SHELFLIFE");
            }
            if(ret.equals("OK")){
                FoodInStorageEntity fise = new FoodInStorageEntity();
                fise.setData(req, st, fe);
                fise.setShelfLife(req.shelfLife);
                storageService.putFood(fise);
                return requestService.returnStoreList(true, "FOOD ADDED.", findStoreListByUser(user), "shops");
            }
            return requestService.returnInfo(false, ret);

        }
        return requestService.returnInfo(false, "INVALID USER");
    }

    @PostMapping("/clearStorage")
    public JsonObject clearStorage(HttpServletRequest request, @RequestBody @Valid IdModel req) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        StorageEntity st = storageService.findById(req.id);
        if(user == null){
            return requestService.returnInfo(false, "INVALID USER");
        }
        if(user.getRoleEntity().getId() == 1){
        } else {
            if(st == null || !st.getStoreEntity().getUserEntity().equals(user)){
                return requestService.returnInfo(false, "INVALID STORAGE");
            }
            List<FoodInStorageEntity> lfise = storageService.findFoodInStorageByStorageEntity(st);
            for(FoodInStorageEntity fise : lfise){
                storageService.foodInStorageDeleteById(fise.getId());
            }
            return requestService.returnStoreList(true, "STORAGE CLEANED", findStoreListByUser(user), "shops");
        }
        return requestService.returnInfo(false, "INVALID USER");
    }

    @PostMapping("/removeFoodFromStorage")
    public String removeFoodFromStorage(HttpServletRequest request, @RequestBody @Valid IdModel req) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        FoodInStorageEntity fise = storageService.findFoodInStorageById(req.id);
        if(user == null){
            return "INVALID USER";
        }
        if(user.getRoleEntity().getId() == 1){
        } else {
            if(fise == null){
                return "INVALID FOOD POSITION";
            }
            if(fise.getStorageEntity().getStoreEntity().getUserEntity().equals(user)){
                return "INVALID USER";
            }

            storageService.foodInStorageDeleteById(fise.getId());

            return "FOOD REMOVED";
        }
        return "INVALID USER";
    }

    @SneakyThrows
    @PostMapping("/updateFoodInStorage")
    public String updateFoodInStorage(HttpServletRequest request, @RequestBody @Valid PutFoodRequest req) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        StorageEntity st = storageService.findById(req.storageId);
        FoodEntity fe = foodService.findById(req.foodId);
        FoodInStorageEntity fise = storageService.findFoodInStorageById(req.id);
        if(user == null){
            return "INVALID USER";
        }
        if(user.getRoleEntity().getId() == 1){
        } else {

            if(fise == null){
                return "INVALID FOOD POSITION";
            }
            if(st == null || !st.getStoreEntity().getUserEntity().equals(user)) {
                return "INVALID STORAGE";
            }
            String ret = ValidateService.validatePutFood(req, fe, st,
                    storageService.getFreeSpaceForUpdate(storageService.findFoodInStorageByStorageEntity(st), st,fise,
                            orderService.findByPurchaser(st.getStoreEntity()), orderService.findAutoOrderByStorage(st)));
            if(req.shelfLife != null){
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                if(sdf.parse(req.shelfLife).before(date)){
                    return "INVALID SHELFLIFE";
                }
            }
            if(ret.equals("OK")){
                fise.setData(req, st, fe);
                if(req.shelfLife != null){
                    fise.setShelfLife(req.shelfLife);
                }
                storageService.putFood(fise);
                return ret;
            }
            return ret;

        }
        return "INVALID USER";
    }

    @PostMapping("/inProgressOrder")
    public JsonObject inProgressOrder(HttpServletRequest request, @RequestBody @Valid IdModel req) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        OrderEntity orderEntity = orderService.findById(req.id);
        FoodInStorageEntity foodInStorageEntity= storageService.findFoodInStorageById(req.foodInStorageId);
        if(user == null){
            return requestService.returnInfo(false, "INVALID USER");
        }
        if(user.getRoleEntity().getId() == 1){
        } else {
            if(orderEntity == null || orderEntity.getProvider().equals(user)){
                return requestService.returnInfo(false, "INVALID USER");
            }
            if(orderEntity.getStatusEntity().getId() != 1){
                return requestService.returnInfo(false, "INVALID ORDER");
            }
            if(foodInStorageEntity ==null || !foodInStorageEntity.getStorageEntity().getStoreEntity().getUserEntity().equals(user)){
                return requestService.returnInfo(false, "INVALID FOODINSTORAGEID");
            }
            if(foodInStorageEntity.getQuantity() < orderEntity.getQuantity()){
                return requestService.returnInfo(false, "INVALID INVALID QUANTITY OF FOOD IN STORAGE");
            }
            double q = foodInStorageEntity.getQuantity() - orderEntity.getQuantity();
            foodInStorageEntity.setQuantity(q);
            storageService.putFood(foodInStorageEntity);
            orderEntity.setShelfLife(foodInStorageEntity.getShelfLife());
            orderService.sentOrder(orderEntity);
            return requestService.returnStoreList(true, "ORDER IN PROGRESS", findStoreListByUserForProvider(user), "shops");
        }
        return requestService.returnInfo(false, "INVALID USER");
    }

    @GetMapping("/getFoodList")
    public JsonObject getFoodList(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        if(user == null){
            return requestService.returnInfo(false, "INVALID USER");
        }
        List<FoodEntity>foodEntityList = foodService.getFoodList();

        List<FoodModel> foodModelList = new ArrayList<>();
        for(FoodEntity foodEntity: foodEntityList){
            FoodModel foodModel = new FoodModel(foodEntity.getId(), foodEntity.getName(), foodEntity.getDescription(),
                    foodEntity.getTempEntity().getDesc(), foodEntity.getOccupiedSpace(), foodEntity.isPacked());
            foodModelList.add(foodModel);
        }

        return requestService.returnFoodList(true, "FOOD LIST", foodModelList, "foodList");
    }

    @GetMapping("/getFoodFromProvider")
    public JsonObject getFoodFromProvider(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        if(user == null){
            return requestService.returnInfo(false, "INVALID USER");
        }
        List<StoreEntity>storeEntityList = storeService.findProvidersUserFirst(user);
        List<FoodModel> foodModelList = new ArrayList<>();
        return requestService.returnFoodList(true, "FOOD LIST", foodModelList, "foodList");
    }

    @PostMapping("/getProvidersOfFood")
    public JsonObject getProvidersOfFood(HttpServletRequest request, @RequestBody @Valid IdModel req) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        FoodEntity foodEntity = foodService.findById(req.id);
        if(user == null){
            return requestService.returnInfo(false, "INVALID USER");
        }
        if(foodEntity == null){
            return requestService.returnInfo(false, "INVALID FOOD");
        }
        List<StoreRequest>storeRequestList = storeService.getListOfProvidersByFood(foodEntity);

        return requestService.returnProvidersList(true, "PROVIDERS LIST", storeRequestList, "providersList");
    }

    @GetMapping("/getProviders")
    public JsonObject getProviders(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        if(user == null){
            return requestService.returnInfo(false, "INVALID USER");
        }
        if(user.getRoleEntity().getId() == 1) {
        }else{
            return requestService.returnStoreList(true, "OK", findStoreListByUserForProvider(user), "shops");
        }
        return requestService.returnInfo(false, "SERVER ERROR");
    }



    private List<StoreRequest> findStoreListByUser(UserEntity user){
        List<StoreEntity> storeEntityList = storeService.findStoresByUser(user);
        List<StoreRequest> storeRequestList = new ArrayList<>();
        for(StoreEntity storeEntity : storeEntityList){
            StoreRequest storeRequest = new StoreRequest(storeEntity.getId(), storeEntity.getName(), storeEntity.getLocation(), storeEntity.getRoleEntity().getId());
            storeRequest.storageList = findStorageListByStore(storeEntity);
            storeRequestList.add(storeRequest);
        }
        return storeRequestList;
    }

    private List<StorageRequest> findStorageListByStore(StoreEntity storeEntity){

        List<StorageEntity> storageEntityList= storageService.findByStoreEntity(storeEntity);
        List<StorageRequest> storageRequestList = new ArrayList<>();

        for(StorageEntity storageEntity : storageEntityList){
            StorageRequest storageRequest = new StorageRequest( storageEntity.getId(), storageEntity.getName(), storageEntity.getDescription(),
                    storageEntity.getTempEntity().getDesc(), storageEntity.getCapacity(), storageEntity.getTempEntity().getId());
            List<FoodInStorageEntity> foodInStorageEntityList = storageService.findFoodInStorageByStorageEntity(storageEntity);
            List<FoodInStorageModel> foodInStorageModelList = new ArrayList<>();
            for (FoodInStorageEntity foodInStorageEntity : foodInStorageEntityList){
                FoodInStorageModel foodInStorageModel = new FoodInStorageModel(foodInStorageEntity.getId(), foodInStorageEntity.getPrice(), foodInStorageEntity.getQuantity(),
                        foodInStorageEntity.getShelfLife(), foodInStorageEntity.getStatusId().getName());
                FoodModel foodModel = new FoodModel(foodInStorageEntity.getFoodEntity().getId(), foodInStorageEntity.getFoodEntity().getName(), foodInStorageEntity.getFoodEntity().getDescription(),
                        foodInStorageEntity.getFoodEntity().isPacked(), foodInStorageEntity.getFoodEntity().getOccupiedSpace());
                foodInStorageModel.food = foodModel;
                foodInStorageModelList.add(foodInStorageModel);
            }

            storageRequest.foodInStorageModelList = foodInStorageModelList;
            storageRequest.orderList = findOrderListByStorage(storageEntity);
            storageRequestList.add(storageRequest);
        }
        return storageRequestList;
    }

    private List<OrderModel> findOrderListByStorage(StorageEntity storageEntity){
        List<OrderEntity> orderEntityList = orderService.findByStorageEntity(storageEntity);
        List<OrderModel> orderModelList = new ArrayList<>();
        for (OrderEntity orderEntity : orderEntityList){
            int id = orderEntity.getStatusEntity().getId();
            if(id == 1 || id == 3 || id == 4 || id == 5){
                OrderModel orderModel = new OrderModel(orderEntity.getId(), orderEntity.getDateOfOrder().substring(0, 10).replaceAll("-","."), orderEntity.getStatusEntity().getName(), orderEntity.getProvider().getId(),
                        orderEntity.getPurchaser().getId(), orderEntity.getFoodEntity().getId(), orderEntity.getStorageEntity().getId(), orderEntity.getQuantity(), orderEntity.getPrice(), orderEntity.getProvider().getName(),
                        orderEntity.getPurchaser().getName(), orderEntity.getFoodEntity().getName());
                orderModel.shelfLife = orderEntity.getShelfLife();
                orderModelList.add(orderModel);
            }
        }
        return orderModelList;
    }

    private List<StoreRequest> findStoreListByUserForProvider(UserEntity user){
        List<StoreEntity> storeEntityList = storeService.findProvidersByUser(user);
        List<StoreRequest> storeRequestList = new ArrayList<>();
        for(StoreEntity storeEntity : storeEntityList){
            StoreRequest storeRequest = new StoreRequest(storeEntity.getId(), storeEntity.getName(), storeEntity.getLocation(), storeEntity.getRoleEntity().getId());
            storeRequest.storageList = findStorageListByStoreForProvider(storeEntity);
            storeRequest.orderList = findOrderListByStore(storeEntity);
            storeRequestList.add(storeRequest);
        }
        return storeRequestList;
    }

    private List<StorageRequest> findStorageListByStoreForProvider(StoreEntity storeEntity){

        List<StorageEntity> storageEntityList= storageService.findByStoreEntity(storeEntity);
        List<StorageRequest> storageRequestList = new ArrayList<>();

        for(StorageEntity storageEntity : storageEntityList){
            StorageRequest storageRequest = new StorageRequest( storageEntity.getId(), storageEntity.getName(), storageEntity.getDescription(),
                    storageEntity.getTempEntity().getDesc(), storageEntity.getCapacity(), storageEntity.getTempEntity().getId());
            List<FoodInStorageEntity> foodInStorageEntityList = storageService.findFoodInStorageByStorageEntity(storageEntity);
            List<FoodInStorageModel> foodInStorageModelList = new ArrayList<>();
            for (FoodInStorageEntity foodInStorageEntity : foodInStorageEntityList){
                FoodInStorageModel foodInStorageModel = new FoodInStorageModel(foodInStorageEntity.getId(), foodInStorageEntity.getPrice(), foodInStorageEntity.getQuantity(),
                        foodInStorageEntity.getShelfLife(), foodInStorageEntity.getStatusId().getName());
                FoodModel foodModel = new FoodModel(foodInStorageEntity.getFoodEntity().getId(), foodInStorageEntity.getFoodEntity().getName(), foodInStorageEntity.getFoodEntity().getDescription(),
                        foodInStorageEntity.getFoodEntity().isPacked(), foodInStorageEntity.getFoodEntity().getOccupiedSpace());
                foodInStorageModel.food = foodModel;
                foodInStorageModelList.add(foodInStorageModel);
            }

            storageRequest.foodInStorageModelList = foodInStorageModelList;
            storageRequestList.add(storageRequest);
        }
        return storageRequestList;
    }

    private List<OrderModel> findOrderListByStore(StoreEntity storeEntity){
        List<OrderEntity> orderEntityList = orderService.findByProvider(storeEntity);
        List<OrderModel> orderModelList = new ArrayList<>();
        for (OrderEntity orderEntity : orderEntityList){
            int id = orderEntity.getStatusEntity().getId();
            if(id == 1 || id == 3 || id == 4 || id == 5){
                OrderModel orderModel = new OrderModel(orderEntity.getId(), orderEntity.getDateOfOrder().substring(0, 10).replaceAll("-","."), orderEntity.getStatusEntity().getName(), orderEntity.getProvider().getId(),
                        orderEntity.getPurchaser().getId(), orderEntity.getFoodEntity().getId(), orderEntity.getStorageEntity().getId(), orderEntity.getQuantity(), orderEntity.getPrice(), orderEntity.getProvider().getName(),
                        orderEntity.getPurchaser().getName(), orderEntity.getFoodEntity().getName());
                orderModel.shelfLife = orderEntity.getShelfLife();
                orderModelList.add(orderModel);
            }
        }
        return orderModelList;
    }
}

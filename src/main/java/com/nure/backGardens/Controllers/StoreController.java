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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = {"*"} )
@RestController
public class StoreController {

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private TmpService tmpService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private FoodService foodService;
    @Autowired
    private RequestService requestService;
    @Autowired
    private CellService cellService;

    @PostMapping("/registerStore")
    public JsonObject registerStore(HttpServletRequest request, @RequestBody @Valid StoreRequest req) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        if(user == null){
            return requestService.returnInfo(false, "INVALID USER");
        }
        if(user.getRoleEntity().getId() == 1) {
        }else{
            if(ValidateService.validateStore(req)){
                StoreEntity st = new StoreEntity();
                st.setName(req.name);
                st.setLocation(req.location);
                st.setUserEntity(user);
                storeService.createStore(st);
                return requestService.returnStoreList(true, "SHOP CREATED", findStoreListByUser(user), "shops");
            }
            return requestService.returnInfo(false, "INVALID DATA FORMAT");
        }
        return requestService.returnInfo(false, "INVALID DATA FORMAT");
    }

    @PostMapping("/updateStoreInfo")
    public JsonObject changeStoreInfo(HttpServletRequest request, @RequestBody @Valid StoreRequest req) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        StoreEntity st = storeService.findById(req.id);
        if(user == null){
            return requestService.returnInfo(false, "INVALID USER");
        }
        if(user.getRoleEntity().getId() == 1){

        } else {
            if( st.getUserEntity().equals(user) ) {
                if(st == null){
                    return requestService.returnInfo(false, "INVALID SHOP");
                }
                if(ValidateService.validateStore(req)) {
                    st.setName(req.name);
                    st.setLocation(req.location);
                    storeService.updateStore(st);
                    return requestService.returnStoreList(true, "SHOP UPDATED", findStoreListByUser(user), "shops");
                }
                return requestService.returnInfo(false, "INVALID DATA FORMAT");
            }
        }
        return requestService.returnInfo(false, "SERVER ERROR");
    }

    @PostMapping("/createStorage")
    public JsonObject createStorage(HttpServletRequest request, @RequestBody @Valid StorageRequest req) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        StoreEntity se = storeService.findById(req.storeId);
        if(user == null){
            return requestService.returnInfo(false, "INVALID USER");
        }
        if(user.getRoleEntity().getId() == 1){

        } else {
            TmpEntity tmp = tmpService.getById(req.tempId);
            if(tmp == null){
                return requestService.returnInfo(false, "INVALID TEMP");
            }
            String ret = ValidateService.validateStorage(req);
            req.id = user.getId();
            if(se == null){
                return requestService.returnInfo(false, "INVALID STORE");
            }
            if(ret.equals("OK")) {
                StorageEntity st = new StorageEntity();
                st.setFields(req, tmp);
                st.setTempEntity(tmp);
                st.setCapacity(req.capacity);
                st.setStoreEntity(se);
                storageService.createStorage(st);
                return requestService.returnStorageList(true, "STORAGE CREATED", findStorageListByStore(se), "storageList");
            }
            return requestService.returnInfo(false, ret);
        }
        return requestService.returnInfo(false, "INVALID USER");
    }

    @PostMapping("/updateStorage")
    public JsonObject updateStorage(HttpServletRequest request, @RequestBody @Valid StorageRequest req) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        StorageEntity st = storageService.findById(req.id);
        if(user == null){
            return requestService.returnInfo(false, "INVALID USER");
        }
        if(user.getRoleEntity().getId() == 1){
        } else {
            if(st.getStoreEntity().getUserEntity().equals(user)) {
                TmpEntity tmp = tmpService.getById(req.id);
                if(tmp == null){
                    return requestService.returnInfo(false, "INVALID TEMP");
                }
                String ret = ValidateService.validateStorage(req);
                if(ret.equals("OK")) {
                    st.setFields(req, tmp);
                    storageService.updateStorage(st);
                    return requestService.returnStorageList(true, "STORAGE UPDATED", findStorageListByStore(st.getStoreEntity()), "storageList");
                }
                return requestService.returnInfo(false, ret);
            }
        }

        return requestService.returnInfo(false, "INVALID USER");
    }

    @SneakyThrows
    @PostMapping("/createOrder")
    public JsonObject createOrder(HttpServletRequest request, @RequestBody @Valid OrderModel req) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        StorageEntity storageEntity = storageService.findById(req.storageId);
        StoreEntity purchaser = storageEntity.getStoreEntity();
        StoreEntity provider = storeService.findById(req.providerId);
        FoodEntity foodEntity = foodService.findById(req.foodId);

        if(user == null){
            return requestService.returnInfo(false, "INVALID USER");
        }
        if(user.getRoleEntity().getId() == 1){
        } else {
            if(purchaser == null || !purchaser.getUserEntity().equals(user)|| purchaser.getRoleEntity().getId() != 3){
                return requestService.returnInfo(false, "INVALID USER");
            }
            if(provider == null || provider.getRoleEntity().getId() != 4){
                return requestService.returnInfo(false, "INVALID PROVIDER");
            }
            if(storageEntity == null || storageEntity.getStoreEntity().equals(provider)){
                return requestService.returnInfo(false, "INVALID STORAGE");
            }
            if(foodEntity == null){
                return requestService.returnInfo(false, "INVALID FOOD");
            }
            if(req.quantity * foodEntity.getOccupiedSpace() > storageService.getFreeSpace(storageService.findFoodInStorageByStorageEntity(storageEntity), storageEntity,
                    orderService.findByPurchaser(storageEntity.getStoreEntity()), orderService.findAutoOrderByStorage(storageEntity))){
                return requestService.returnInfo(false, "INVALID SPACE");
            }
            OrderEntity orderEntity = new OrderEntity();
            DateFormat df = new SimpleDateFormat("dd/MM/YYYY");
            String data = df.format(new Date());
            System.out.println(data);
            orderEntity.setDateOfOrder(data);
            orderEntity.setProvider(provider);
            orderEntity.setPurchaser(purchaser);
            orderEntity.setFoodEntity(foodEntity);
            orderEntity.setStorageEntity(storageEntity);
            orderEntity.setQuantity(req.quantity);
            orderEntity.setPrice(req.price);
            orderService.save(orderEntity);
            return requestService.returnStorageList(true, "ORDER CREATED", findStorageListByStore(storageEntity.getStoreEntity()), "storageList");
        }
        return requestService.returnInfo(false, "INVALID USER");
    }

    @PostMapping("/finishOrder")
    public JsonObject finishOrder(HttpServletRequest request, @RequestBody @Valid IdModel req) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        OrderEntity orderEntity = orderService.findById(req.id);
        if(user == null){
            return requestService.returnInfo(false, "INVALID USER");
        }
        if(user.getRoleEntity().getId() == 1){
        } else {
            if(orderEntity == null || orderEntity.getPurchaser().equals(user)){
                return requestService.returnInfo(false, "INVALID USER");
            }
            if(orderEntity.getStatusEntity().getId() != 5){
                return requestService.returnInfo(false, "INVALID ORDER");
            }
            orderService.finishOrder(orderEntity);
            FoodInStorageEntity foodInStorageEntity = new FoodInStorageEntity();
            foodInStorageEntity.setFoodEntity(orderEntity.getFoodEntity());
            foodInStorageEntity.setStorageEntity(orderEntity.getStorageEntity());
            foodInStorageEntity.setQuantity(orderEntity.getQuantity());
            foodInStorageEntity.setShelfLife(orderEntity.getShelfLife());
            foodInStorageEntity.setPrice(orderEntity.getPrice());
            storageService.putFood(foodInStorageEntity);
            return requestService.returnStoreList(true, "ORDER FINISHED. FOOD ADDED.", findStoreListByUser(user), "shops");
        }
        return requestService.returnInfo(false, "INVALID USER");
    }

    @PostMapping("/cancelOrder")
    public JsonObject cancelOrder(HttpServletRequest request, @RequestBody @Valid ListIdModel req) {

        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));

        if(user == null){
            return requestService.returnInfo(false, "INVALID USER");
        }
        if(user.getRoleEntity().getId() == 1){
        } else {
            for(IdModel id : req.idList){
                OrderEntity orderEntity = orderService.findById(id.id);
                if(orderEntity == null || orderEntity.getPurchaser().equals(user)){
                    return requestService.returnInfo(false, "ERROR. PART OF ORDERS CANCELED");
                }
                if(orderEntity.getStatusEntity().getId() != 1){
                    return requestService.returnInfo(false, "ERROR. PART OF ORDERS CANCELED");
                }
                orderService.cancelOrder(orderEntity);

            }
            return requestService.returnStoreList(true, "ORDER CANCELED", findStoreListByUser(user), "shops");
        }
        return requestService.returnInfo(false, "INVALID USER");
    }

    @PostMapping("/createAutoOrder")
    public String createAutoOrder(HttpServletRequest request, @RequestBody @Valid AutoOrderRequest req) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        StorageEntity storageEntity = storageService.findById(req.storageId);
        FoodEntity foodEntity = foodService.findById(req.foodId);
        StoreEntity provider = storeService.findById(req.provider);
        if(user == null){
            return "INVALID USER";
        }
        if(user.getRoleEntity().getId() == 1){
        } else {
            if(storageEntity == null || !storageEntity.getStoreEntity().getUserEntity().equals(user) || storageEntity.getStoreEntity().getRoleEntity().getId() != 3){
                return "INVALID STORAGE";
            }
            if(foodEntity == null){
                return "INVALID FOOD";
            }
            if(provider == null || provider.getRoleEntity().getId() != 4){
                return "INVALID PROVIDER";
            }
            if(req.quantityInStorage < 0){
                return  "INVALID SPACE";
            }
            if(req.price < 0){
                return  "INVALID PRICE";
            }
            if(storageService.getFreeSpace(storageService.findFoodInStorageByStorageEntity(storageEntity), storageEntity,
                    orderService.findByPurchaser(storageEntity.getStoreEntity()), orderService.findAutoOrderByStorage(storageEntity)) < req.quantityToOrder * foodEntity.getOccupiedSpace()){
                return  "INVALID SPACE";
            }
            AutoOrderEntity autoOrderEntity = new AutoOrderEntity(foodEntity, req.quantityInStorage, req.quantityToOrder, provider, req.price);
            autoOrderEntity.setStorageEntity(storageEntity);
            orderService.saveAutoOrder(autoOrderEntity);
            return "AUTOORDER CREATED";
        }
        return "INVALID USER";
    }

    @PostMapping("/updateAutoOrder")
    public String updateAutoOrder(HttpServletRequest request, @RequestBody @Valid AutoOrderRequest req) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        AutoOrderEntity autoOrderEntity = orderService.findAutoOrderById(req.id);
        StoreEntity provider = storeService.findById(req.provider);
        if(user == null){
            return "INVALID USER";
        }
        if(user.getRoleEntity().getId() == 1){
        } else {
            if(req.price < 0){
                return  "INVALID PRICE";
            }
            if(autoOrderEntity == null || !autoOrderEntity.getStorageEntity().getStoreEntity().getUserEntity().equals(user)){
                return "INVALID AUTOORDER";
            }
            if(req.quantityInStorage < 0){
                return  "INVALID SPACE";
            }
            if(provider == null || provider.getRoleEntity().getId() != 4){
                return "INVALID PROVIDER";
            }
            if(storageService.getFreeSpaceForAutoOrder(storageService.findFoodInStorageByStorageEntity(autoOrderEntity.getStorageEntity()), autoOrderEntity.getStorageEntity(),
                    orderService.findByPurchaser(autoOrderEntity.getStorageEntity().getStoreEntity()),
                    orderService.findAutoOrderByStorage(autoOrderEntity.getStorageEntity()), req.id) < req.quantityToOrder *  autoOrderEntity.getFoodEntity().getOccupiedSpace()){
                return  "INVALID SPACE";
            }
            autoOrderEntity.updateAutoOrder(req, provider);
            orderService.saveAutoOrder(autoOrderEntity);
            return "AUTOORDER UPDATED";
        }
        return "INVALID USER";
    }

    @PostMapping("/registerMachine")
    public JsonObject registerMachine(HttpServletRequest request, @RequestBody @Valid StoreRequest req) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        if(user == null){
            return requestService.returnInfo(false, "INVALID USER");
        }
        if(user.getRoleEntity().getId() == 1) {
        }else{
            if(ValidateService.validateStore(req)){
                StoreEntity st = new StoreEntity();
                st.setName(req.name);
                st.setLocation(req.location);
                st.setUserEntity(user);
                storeService.createMachine(st);
                return requestService.returnStoreList(true, "OK", findMachinesListByUser(user), "machines");
            }
            return requestService.returnInfo(false, "INVALID DATA FORMAT");
        }
        return requestService.returnInfo(false, "INVALID USER");
    }

    @PostMapping("/updateMachine")
    public JsonObject updateMachine(HttpServletRequest request, @RequestBody @Valid StoreRequest req) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        StoreEntity storeEntity = storeService.findById(req.id);
        if(user == null){
            return requestService.returnInfo(false, "INVALID USER");
        }
        if(user.getRoleEntity().getId() == 1) {
        }else{
            if(storeEntity == null){
                return requestService.returnInfo(false, "INVALID VENDING MACHINE");
            }
            if(ValidateService.validateStore(req)){
                storeEntity.setName(req.name);
                storeEntity.setLocation(req.location);
                storeService.updateStore(storeEntity);

                return requestService.returnStoreList(true, "OK", findMachinesListByUser(user), "machines");
            }
            return requestService.returnInfo(false, "INVALID DATA FORMAT");
        }
        return requestService.returnInfo(false, "INVALID USER");
    }

    @GetMapping("/getMachines")
    public JsonObject getMachines(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        if(user == null){
            return requestService.returnInfo(false, "INVALID USER");
        }
        if(user.getRoleEntity().getId() == 1) {
        }else{
            return requestService.returnStoreList(true, "OK", findMachinesListByUser(user), "machines");
        }
        return requestService.returnInfo(false, "SERVER ERROR");
    }

    @GetMapping("/getMachinesBack")
    public List<StoreRequest> getMachinesBack(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        if(user == null){
            return null;
        }
        if(user.getRoleEntity().getId() == 1) {
        }else{
            return findMachinesListByUser(user);
        }
        return null;
    }

    @GetMapping("/getStores")
    public JsonObject getStores(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        if(user == null){
            return requestService.returnInfo(false, "INVALID USER");
        }
        if(user.getRoleEntity().getId() == 1) {
        }else{
            return requestService.returnStoreList(true, "OK", findStoreListByUser(user), "shops");
        }
        return requestService.returnInfo(false, "SERVER ERROR");
    }

    @GetMapping("/getFoodThatProvide")
    public JsonObject getFoodThatProvide(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        if(user == null){
            return requestService.returnInfo(false, "INVALID USER");
        }
        List<StoreRequest>storeRequestList = storeService.getListOfProvidersByStore(user);

        return requestService.returnProvidersList(true, "PROVIDERS LIST", storeRequestList, "providersList");
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

    private List<StoreRequest> findMachinesListByUser(UserEntity user){
        List<StoreEntity> storeEntityList = storeService.findMachinesByUser(user);
        List<StoreRequest> storeRequestList = new ArrayList<>();
        for(StoreEntity storeEntity : storeEntityList){
            StoreRequest storeRequest = new StoreRequest(storeEntity.getId(), storeEntity.getName(), storeEntity.getLocation(), storeEntity.getRoleEntity().getId());
            List<CellEntity> cellEntityList = cellService.findCellByStoreEntity(storeEntity);
            List<CellRequest> cellRequestList = new ArrayList<>();
            for(CellEntity cellEntity : cellEntityList){
                ListToProvideEntity listToProvideEntity = cellEntity.getListToProvideEntity();
                CellRequest cellRequest = new CellRequest(cellEntity.getId(),
                        cellEntity.getStoreEntity().getId(), cellEntity.getStatusEntity().getName(), cellEntity.getNumber());
                if(listToProvideEntity != null){
                    cellRequest.updCellRequest(listToProvideEntity.getFoodEntity().getTempEntity().getDesc(), listToProvideEntity.getFoodEntity().getId(), cellEntity.getShelfLife(),
                            cellEntity.getPrice(), listToProvideEntity.getFoodEntity().getName(), listToProvideEntity.getFoodEntity().getDescription(), cellEntity.getWeight(),
                            listToProvideEntity.getStoreEntity().getId(), listToProvideEntity.getPrice(), listToProvideEntity.getStoreEntity().getName(),
                            listToProvideEntity.getStoreEntity().getUserEntity().getId(), listToProvideEntity.getFoodEntity().isPacked(),  listToProvideEntity.getStoreEntity().getLocation());
                }

                cellRequestList.add(cellRequest);
            }
            storeRequest.cellList = cellRequestList;
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

}

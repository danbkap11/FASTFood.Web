package com.nure.backGardens.Controllers;

import com.google.gson.JsonObject;
import com.nure.backGardens.Models.CellRequest;
import com.nure.backGardens.Models.IdModel;
import com.nure.backGardens.Models.StoreRequest;
import com.nure.backGardens.Models.UserRequest;
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
public class CellController {
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private TmpService tmpService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private FoodService foodService;
    @Autowired
    private CellService cellService;
    @Autowired
    private RequestService requestService;


    @SneakyThrows
    @PostMapping("/createEmptyCell")
    public JsonObject createEmptyCell(HttpServletRequest request, @RequestBody @Valid IdModel req) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        StoreEntity storeEntity = storeService.findById(req.id);
        if(user == null){
            return requestService.returnInfo(false, "INVALID USER");
        }
        if(user.getRoleEntity().getId() == 1) {
        }else{
            if(!storeEntity.getUserEntity().equals(user) || storeEntity.getRoleEntity().getId() != 5){
                return requestService.returnInfo(false, "INVALID MACHINE");
            }
            CellEntity cellEntity = new CellEntity(cellService.getNextCellNumber(storeEntity), storeEntity);
            cellService.cellToEmpty(cellEntity);
            CellEntity cellEntitytoreeq = cellService.findByStoreEntityAndNumber(storeEntity, cellEntity.getNumber());
            CellRequest cellRequest = new CellRequest(cellEntitytoreeq.getId(),
                    cellEntitytoreeq.getStoreEntity().getId(), cellEntitytoreeq.getStatusEntity().getName(), cellEntitytoreeq.getNumber());
            return requestService.returnCell(true, "CELL CREATED", cellRequest, "cell");
        }
        return requestService.returnInfo(false, "INVALID USER");
    }

    @PostMapping("/updateCellInfo")
    public JsonObject updateCellInfo(HttpServletRequest request, @RequestBody @Valid CellRequest req) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        FoodEntity foodEntity = foodService.findById(req.foodId);
        CellEntity cellEntity = cellService.findById(req.id);
        StoreEntity provider = storeService.findById(req.providerId);
        if(user == null){
            return requestService.returnInfo(false, "INVALID USER");
        }
        if(user.getRoleEntity().getId() == 1) {
        }else{
            if(!cellEntity.getStoreEntity().getUserEntity().equals(user)){
                return requestService.returnInfo(false, "INVALID MACHINE");
            }
            if(cellEntity == null ){
                return requestService.returnInfo(false, "INVALID CELL");
            }
            if(foodEntity == null ){
                return requestService.returnInfo(false, "INVALID FOOD");
            }
            if(provider == null ){
                return requestService.returnInfo(false, "INVALID PROVIDER");
            }
            String resp = ValidateService.validateCell(req);
            if(resp.equals("VALID")){
                ListToProvideEntity  listToProvideEntity = storeService.findListToProvideEntityByStoreAndFood(foodEntity, provider);
                if(listToProvideEntity == null){
                    return requestService.returnInfo(false, "INVALID PROVIDER FOOD");
                }
                cellEntity.updateCell(req, listToProvideEntity);
                cellService.cellToSentRequest(cellEntity);
                AutoOrderEntity autoOrderEntity = new AutoOrderEntity(foodEntity, 0, foodEntity.isPacked()?1:req.weight, provider, req.price);
                autoOrderEntity.setCellEntity(cellEntity);
                orderService.saveAutoOrder(autoOrderEntity);
                CellRequest cellRequest = new CellRequest(cellEntity.getId(),
                        cellEntity.getStoreEntity().getId(), cellEntity.getStatusEntity().getName(), cellEntity.getNumber());
                if(listToProvideEntity != null){
                    cellRequest.updCellRequest(listToProvideEntity.getFoodEntity().getTempEntity().getDesc(), listToProvideEntity.getFoodEntity().getId(), cellEntity.getShelfLife(),
                            cellEntity.getPrice(), listToProvideEntity.getFoodEntity().getName(), listToProvideEntity.getFoodEntity().getDescription(), cellEntity.getWeight(),
                            listToProvideEntity.getStoreEntity().getId(), listToProvideEntity.getPrice(), listToProvideEntity.getStoreEntity().getName(),
                            listToProvideEntity.getStoreEntity().getUserEntity().getId(), listToProvideEntity.getFoodEntity().isPacked(),  listToProvideEntity.getStoreEntity().getLocation());
                }
                return  requestService.returnCell(true, "CELL UPDATED", cellRequest, "cell");
            }
            return requestService.returnInfo(false, resp);
        }
        return requestService.returnInfo(false, "INVALID USER");
    }

    @SneakyThrows
    @PostMapping("/cellToReady")
    public String cellToReady(HttpServletRequest request, @RequestBody @Valid CellRequest req) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        CellEntity cellEntity = cellService.findById(req.id);
        if(user == null){
            return "INVALID USER";
        }
        if(user.getRoleEntity().getId() == 1) {
        }else{
            if(!cellEntity.getStoreEntity().getUserEntity().equals(user)){
                return "INVALID MACHINE";
            }
            if(cellEntity == null ){
                return "INVALID CELL";
            }
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            if(req.shelfLife == null || sdf.parse(req.shelfLife).before(date)){
                return "INVALID SHELFLIFE";
            }
            cellEntity.setShelfLife(req.shelfLife);

            cellService.cellToReady(cellEntity);

            return "CELL READY";
        }
        return "INVALID USER";
    }

    @PostMapping("/cellToSentRequest")
    public String cellToSentRequest(HttpServletRequest request, @RequestBody @Valid IdModel req) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        CellEntity cellEntity = cellService.findById(req.id);
        if(user == null){
            return "INVALID USER";
        }
        if(user.getRoleEntity().getId() == 1) {
        }else{
            if(!cellEntity.getStoreEntity().getUserEntity().equals(user)){
                return "INVALID MACHINE";
            }
            if(cellEntity == null ){
                return "INVALID CELL";
            }

            cellService.cellToSentRequest(cellEntity);

            return "CELL READY TO SENT";
        }
        return "INVALID USER";
    }

    @PostMapping("/cellToEmpty")
    public JsonObject cellToEmpty(HttpServletRequest request, @RequestBody @Valid IdModel req) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        CellEntity cellEntity = cellService.findById(req.id);
        if(user == null){
            return requestService.returnInfo(false, "INVALID USER");
        }
        if(user.getRoleEntity().getId() == 1) {
        }else{
            if(!cellEntity.getStoreEntity().getUserEntity().equals(user)){
                return requestService.returnInfo(false, "INVALID MACHINE");
            }
            if(cellEntity == null ){
                return requestService.returnInfo(false, "INVALID CELL");
            }

            cellService.cellToEmpty(cellEntity);

            CellRequest cellRequest = new CellRequest(cellEntity.getId(),
                    cellEntity.getStoreEntity().getId(), cellEntity.getStatusEntity().getName(), cellEntity.getNumber());

            return  requestService.returnCell(true, "CELL CLEARED", cellRequest, "cell");
        }
        return requestService.returnInfo(false, "INVALID USER");
    }
}
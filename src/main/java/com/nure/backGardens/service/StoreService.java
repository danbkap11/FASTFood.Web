package com.nure.backGardens.service;

import com.nure.backGardens.Models.FoodModel;
import com.nure.backGardens.Models.StoreRequest;
import com.nure.backGardens.entites.*;
import com.nure.backGardens.repository.ListToProvideEntityRepository;
import com.nure.backGardens.repository.RoleEntityRepository;
import com.nure.backGardens.repository.StoreEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StoreService {

    @Autowired
    private StoreEntityRepository storeEntityRepository;
    @Autowired
    private RoleEntityRepository roleEntityRepository;
    @Autowired
    private ListToProvideEntityRepository listToProvideEntityRepository;


    public StoreEntity createStore(StoreEntity storeEntity) {
            RoleEntity storeRole = roleEntityRepository.findByName("ROLE_STORE");
            storeEntity.setRoleEntity(storeRole);
            return storeEntityRepository.save(storeEntity);
    }

    public StoreEntity createProvider(StoreEntity storeEntity) {
        RoleEntity storeRole = roleEntityRepository.findByName("ROLE_PROVIDER");
        storeEntity.setRoleEntity(storeRole);
        return storeEntityRepository.save(storeEntity);
    }

    public StoreEntity createMachine(StoreEntity storeEntity) {
        RoleEntity storeRole = roleEntityRepository.findByName("ROLE_MACHINE");
        storeEntity.setRoleEntity(storeRole);
        return storeEntityRepository.save(storeEntity);
    }

    public StoreEntity updateStore(StoreEntity storeEntity) {
        return storeEntityRepository.save(storeEntity);
    }

    public StoreEntity findById(int id) {
        return storeEntityRepository.findById(id);
    }

    public List<StoreEntity> findMachinesByUser(UserEntity userEntity){
        return storeEntityRepository.findByUserEntityAndRoleEntity(userEntity, roleEntityRepository.findById(5));
    }

    public List<StoreEntity> findProvidersUserFirst(UserEntity userEntity){
        List<StoreEntity> storeEntityList = storeEntityRepository.findByUserEntityAndRoleEntity(userEntity, roleEntityRepository.findById(4));
        storeEntityList.addAll(storeEntityRepository.findByRoleEntityAndUserEntityIsNot(roleEntityRepository.findById(4), userEntity));
        return storeEntityList;
    }

    public List<StoreRequest> getListOfProvidersByFood(FoodEntity foodEntity){
        List<ListToProvideEntity> listToProvideEntityList = listToProvideEntityRepository.findByFoodEntity(foodEntity);
        List<StoreRequest> storeRequestList = new ArrayList<>();
        for(ListToProvideEntity listToProvideEntity : listToProvideEntityList){
            StoreRequest storeRequest = new StoreRequest(listToProvideEntity.getStoreEntity().getId(), listToProvideEntity.getStoreEntity().getName(),
                    listToProvideEntity.getStoreEntity().getLocation(), listToProvideEntity.getStoreEntity().getRoleEntity().getId());
            storeRequest.price = listToProvideEntity.getPrice();
            storeRequest.userId = listToProvideEntity.getStoreEntity().getUserEntity().getId();
            storeRequestList.add(storeRequest);
        }
        return storeRequestList;
    }

    public ListToProvideEntity findListToProvideEntityByStoreAndFood(FoodEntity foodEntity, StoreEntity storeEntity){
        List<ListToProvideEntity> listToProvideEntityList = listToProvideEntityRepository.findByStoreEntityAndFoodEntity(storeEntity, foodEntity);
        if(listToProvideEntityList != null ){
            if(listToProvideEntityList.size() == 0)
                return null;
        }
        return listToProvideEntityList.get(0);
    }

    public List<StoreEntity> findStoresByUser(UserEntity userEntity){
        return storeEntityRepository.findByUserEntityAndRoleEntity(userEntity, roleEntityRepository.findById(3));
    }

    public List<StoreEntity> findProvidersByUser(UserEntity userEntity){
        return storeEntityRepository.findByUserEntityAndRoleEntity(userEntity, roleEntityRepository.findById(4));
    }

    public List<StoreRequest> getListOfProvidersByStore(UserEntity userEntity){
        List<ListToProvideEntity> listToProvideEntityList = new ArrayList<>();
        for(StoreEntity storeEntity : storeEntityRepository.findByUserEntityAndRoleEntity(userEntity, roleEntityRepository.findById(4))){
            listToProvideEntityList.addAll(listToProvideEntityRepository.findByStoreEntity(storeEntity));
        }

        List<StoreRequest> storeRequestList = new ArrayList<>();
        for(ListToProvideEntity listToProvideEntity : listToProvideEntityList){
            StoreRequest storeRequest = new StoreRequest(listToProvideEntity.getId(), listToProvideEntity.getStoreEntity().getName(),
                    listToProvideEntity.getStoreEntity().getLocation(), listToProvideEntity.getStoreEntity().getRoleEntity().getId());
            FoodEntity foodEntity = listToProvideEntity.getFoodEntity();
            FoodModel foodModel = new FoodModel(foodEntity.getId(), foodEntity.getName(), foodEntity.getDescription(),
                    foodEntity.getTempEntity().getDesc(), foodEntity.getOccupiedSpace(), foodEntity.isPacked());
            storeRequest.food = foodModel;
            storeRequest.price = listToProvideEntity.getPrice();
            storeRequest.userId = listToProvideEntity.getStoreEntity().getUserEntity().getId();
            storeRequest.providerId = listToProvideEntity.getStoreEntity().getId();
            storeRequestList.add(storeRequest);
        }
        return storeRequestList;
    }


}

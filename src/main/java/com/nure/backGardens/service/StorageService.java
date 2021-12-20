package com.nure.backGardens.service;

import com.nure.backGardens.Models.StorageRequest;
import com.nure.backGardens.Models.StoreRequest;
import com.nure.backGardens.entites.*;
import com.nure.backGardens.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StorageService {

    @Autowired
    private StorageEntityRepository storageEntityRepository;
    @Autowired
    private StoreEntityRepository storeEntityRepository;
    @Autowired
    private FoodInStorageEntityRepository foodInStorageEntityRepository;
    @Autowired
    private StatusEntityRepository statusEntityRepository;


    public StorageEntity createStorage(StorageEntity storageEntity) {
        return storageEntityRepository.save(storageEntity);
    }

    public StorageEntity updateStorage(StorageEntity storageEntity) {
        return storageEntityRepository.save(storageEntity);
    }
    public FoodInStorageEntity putFood(FoodInStorageEntity fise) {
        fise.setStatusId(statusEntityRepository.findByName("READY"));
        return foodInStorageEntityRepository.save(fise);
    }
    public FoodInStorageEntity orderFood(FoodInStorageEntity fise) {
        fise.setStatusId(statusEntityRepository.findByName("SENT_REQUEST"));
        return foodInStorageEntityRepository.save(fise);
    }


    public StorageEntity findById(int id) {
        return storageEntityRepository.findById(id);
    }

    public List<FoodInStorageEntity> findFoodInStorageByStorageEntity(StorageEntity se){
        return foodInStorageEntityRepository.findByStorageEntity(se);
    }
    public double getFreeSpace(List<FoodInStorageEntity> lfis, StorageEntity st, List<OrderEntity> orderEntityList, List<AutoOrderEntity> autoOrderEntityList){
        double freeCapacity = st.getCapacity();
        for(FoodInStorageEntity fis: lfis) {
            freeCapacity -= fis.getQuantity() * fis.getFoodEntity().getOccupiedSpace();
        }
        for(OrderEntity orderEntity: orderEntityList){
            if(orderEntity.getStatusEntity().getId() != 3 && orderEntity.getStatusEntity().getId() != 4){
                freeCapacity -= orderEntity.getQuantity() * orderEntity.getFoodEntity().getOccupiedSpace();
            }
        }
        for(AutoOrderEntity autoOrderEntity: autoOrderEntityList){
            int q = 0;
            for(FoodInStorageEntity fis: lfis) {
                if(fis.getFoodEntity().equals(autoOrderEntity.getFoodEntity())){
                    q += fis.getQuantity();
                }
            }
            freeCapacity -= (autoOrderEntity.getQuantityInStorage() - q)*autoOrderEntity.getFoodEntity().getOccupiedSpace();
        }
        return freeCapacity;
    }

    public double getFreeSpaceForUpdate(List<FoodInStorageEntity> lfis, StorageEntity st, FoodInStorageEntity fise, List<OrderEntity> orderEntityList, List<AutoOrderEntity> autoOrderEntityList){
        double freeCapacity = st.getCapacity();

        for(FoodInStorageEntity fis: lfis) {
            if(!fis.equals(fise)){
                freeCapacity -= fis.getQuantity() * fis.getFoodEntity().getOccupiedSpace();
            }
        }
        for(OrderEntity orderEntity: orderEntityList){
            if(orderEntity.getStatusEntity().getId() != 3 && orderEntity.getStatusEntity().getId() != 4){
                freeCapacity -= orderEntity.getQuantity() * orderEntity.getFoodEntity().getOccupiedSpace();
            }
        }
        for(AutoOrderEntity autoOrderEntity: autoOrderEntityList){
            int q = 0;
            for(FoodInStorageEntity fis: lfis) {
                if(fis.getFoodEntity().equals(autoOrderEntity.getFoodEntity())){
                    q += fis.getQuantity();
                }
            }
            freeCapacity -= (autoOrderEntity.getQuantityInStorage() - q)*autoOrderEntity.getFoodEntity().getOccupiedSpace();
        }
        return freeCapacity;
    }

    public double getFreeSpaceForAutoOrder(List<FoodInStorageEntity> lfis, StorageEntity st, List<OrderEntity> orderEntityList, List<AutoOrderEntity> autoOrderEntityList, int auotOrderID){
        double freeCapacity = st.getCapacity();
        for(FoodInStorageEntity fis: lfis) {
            freeCapacity -= fis.getQuantity() * fis.getFoodEntity().getOccupiedSpace();
        }
        for(OrderEntity orderEntity: orderEntityList){
            if(orderEntity.getStatusEntity().getId() != 3 && orderEntity.getStatusEntity().getId() != 4){
                freeCapacity -= orderEntity.getQuantity() * orderEntity.getFoodEntity().getOccupiedSpace();
            }
        }
        for(AutoOrderEntity autoOrderEntity: autoOrderEntityList){
            if(auotOrderID != autoOrderEntity.getId()){
                int q = 0;
                for(FoodInStorageEntity fis: lfis) {
                    if(fis.getFoodEntity().equals(autoOrderEntity.getFoodEntity())){
                        q += fis.getQuantity();
                    }
                }
                freeCapacity -= (autoOrderEntity.getQuantityInStorage() - q)*autoOrderEntity.getFoodEntity().getOccupiedSpace();
            }
        }
        return freeCapacity;
    }

    public FoodInStorageEntity findFoodInStorageById(int id){
        return foodInStorageEntityRepository.findById(id);
    }

    public void foodInStorageDeleteById(int id){
        foodInStorageEntityRepository.deleteById(id);
    }

    public int getQuantityOfFoodFromProvider(FoodEntity foodEntity, String shelfLife, StoreEntity provider){
        int q = 0;
        for(StorageEntity se : storageEntityRepository.findByStoreEntity(provider)){
            List<FoodInStorageEntity> foodInStorageEntityList = foodInStorageEntityRepository.findByStorageEntityAndFoodEntityAndShelfLife(se, foodEntity, shelfLife);
            for(FoodInStorageEntity foodInStorageEntity : foodInStorageEntityList){
                if(foodInStorageEntity.getStatusId().getId() == 6){
                    q += foodInStorageEntity.getQuantity();
                }
            }
        }
        return q;
    }

    public List<StorageEntity> findByStoreEntity(StoreEntity storeEntity){
        return storageEntityRepository.findByStoreEntity(storeEntity);
    }

}

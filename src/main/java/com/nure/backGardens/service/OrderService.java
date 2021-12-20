package com.nure.backGardens.service;

import com.nure.backGardens.entites.*;
import com.nure.backGardens.repository.AutoOrderEntityRepository;
import com.nure.backGardens.repository.OrderEntityRepository;
import com.nure.backGardens.repository.StatusEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderEntityRepository orderEntityRepository;
    @Autowired
    private AutoOrderEntityRepository autoOrderEntityRepository;
    @Autowired
    private StatusEntityRepository statusEntityRepository;

    public OrderEntity save(OrderEntity orderEntity) {
        orderEntity.setStatusEntity(statusEntityRepository.findByName("SENT_REQUEST"));
        return orderEntityRepository.save(orderEntity);
    }

    public OrderEntity acceptOrder(OrderEntity orderEntity) {
        orderEntity.setStatusEntity(statusEntityRepository.findByName("ACCEPTED"));
        return orderEntityRepository.save(orderEntity);
    }

    public OrderEntity cancelOrder(OrderEntity orderEntity) {
        orderEntity.setStatusEntity(statusEntityRepository.findByName("CANCELED"));
        return orderEntityRepository.save(orderEntity);
    }
    public OrderEntity sentOrder(OrderEntity orderEntity) {
        orderEntity.setStatusEntity(statusEntityRepository.findByName("INPROGRESS"));
        return orderEntityRepository.save(orderEntity);
    }

    public OrderEntity finishOrder(OrderEntity orderEntity) {
        orderEntity.setStatusEntity(statusEntityRepository.findByName("FINISHED"));
        return orderEntityRepository.save(orderEntity);
    }

    public AutoOrderEntity saveAutoOrder(AutoOrderEntity autoOrderEntity){
        return autoOrderEntityRepository.save(autoOrderEntity);
    }

    public List<OrderEntity> findByPurchaser(StoreEntity se) {
        return orderEntityRepository.findByPurchaser(se);
    }

    public List<OrderEntity> findByStorageEntity(StorageEntity storageEntity) {
        return orderEntityRepository.findByStorageEntity(storageEntity);
    }

    public List<OrderEntity> findByProvider(StoreEntity storeEntity) {
        return orderEntityRepository.findByProvider(storeEntity);
    }

    public List<AutoOrderEntity> findAutoOrderByStorage(StorageEntity se) {
        return autoOrderEntityRepository.findByStorageEntity(se);
    }

    public OrderEntity findById(int id) {
        return orderEntityRepository.findById(id);
    }

    public AutoOrderEntity findAutoOrderById(int id){
        return autoOrderEntityRepository.findById(id);
    }
}
package com.nure.backGardens.service;

import com.nure.backGardens.entites.CellEntity;
import com.nure.backGardens.entites.FoodEntity;
import com.nure.backGardens.entites.StorageEntity;
import com.nure.backGardens.entites.StoreEntity;
import com.nure.backGardens.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CellService {

    @Autowired
    private FoodEntityRepository foodEntityRepository;
    @Autowired
    private StoreEntityRepository storeEntityRepository;
    @Autowired
    private StatusEntityRepository statusEntityRepository;
    @Autowired
    private CellEntityRepository cellEntityRepository;



    public CellEntity cellToWhaiting(CellEntity cellEntity) {
        cellEntity.setStatusEntity(statusEntityRepository.findByName("WHAITING"));
        return cellEntityRepository.save(cellEntity);
    }
    public CellEntity cellToReady(CellEntity cellEntity) {
        cellEntity.setStatusEntity(statusEntityRepository.findByName("READY"));
        return cellEntityRepository.save(cellEntity);
    }
    public CellEntity cellToSentRequest(CellEntity cellEntity) {
        cellEntity.setStatusEntity(statusEntityRepository.findByName("SENT_REQUEST"));
        return cellEntityRepository.save(cellEntity);
    }
    public CellEntity cellToEmpty(CellEntity cellEntity) {
        cellEntity.setStatusEntity(statusEntityRepository.findByName("EMPTY"));
        return cellEntityRepository.save(cellEntity);
    }
    public CellEntity updateCell(CellEntity cellEntity) {
        return cellEntityRepository.save(cellEntity);
    }
    public CellEntity findById(int id) {
        return cellEntityRepository.findById(id);
    }
    public  void  delete(CellEntity cellEntity){
        cellEntityRepository.delete(cellEntity);
    }

    public int getNextCellNumber(StoreEntity storeEntity){
        return cellEntityRepository.findByStoreEntity(storeEntity).size() + 1;
    }
    public List<CellEntity> findCellByStoreEntity(StoreEntity storeEntity){
        return cellEntityRepository.findByStoreEntity(storeEntity);
    }

    public CellEntity findByStoreEntityAndNumber(StoreEntity storeEntity, int number){
        return cellEntityRepository.findByStoreEntityAndNumber(storeEntity, number);
    }

}
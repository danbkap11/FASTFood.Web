package com.nure.backGardens.service;


import com.nure.backGardens.entites.TmpEntity;
import com.nure.backGardens.repository.TmpEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TmpService {

    @Autowired
    private TmpEntityRepository tmpEntity;

    public TmpEntity save(TmpEntity foodEntity) {
        return tmpEntity.save(foodEntity);
    }

    public TmpEntity getByName(String name) {
        return tmpEntity.findByName(name);
    }

    public TmpEntity getById(int id) {
        return tmpEntity.findById(id);
    }


}

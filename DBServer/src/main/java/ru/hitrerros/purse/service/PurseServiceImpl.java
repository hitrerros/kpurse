package ru.hitrerros.purse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hitrerros.purse.model.PurseEntity;
import ru.hitrerros.purse.model.repository.PurseRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class PurseServiceImpl implements PurseService {

    @Autowired
    private  PurseRepository repository;

    @Override
    public PurseEntity getRecord(long id) {
        return repository.getRecord(id);
    }

    @Override
    public PurseEntity saveRecord(PurseEntity record) {
        return  repository.saveRecord(record);
    }

    @Override
    public void deleteRecord(PurseEntity record) {
        repository.deleteRecord(record);
    }


    @Override
    public List<PurseEntity> getRangeOfRecords(LocalDate from, LocalDate to) {
        return repository.getRangeOfRecords(from,to);
    }




}

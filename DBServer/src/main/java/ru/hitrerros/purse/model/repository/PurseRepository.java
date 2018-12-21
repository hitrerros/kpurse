package ru.hitrerros.purse.model.repository;

import ru.hitrerros.purse.model.PurseEntity;

import java.time.LocalDate;
import java.util.List;

public interface PurseRepository {

    PurseEntity getRecord(long id);

    PurseEntity saveRecord(PurseEntity record);

    List<PurseEntity> getRangeOfRecords(LocalDate from, LocalDate to);

    List<PurseEntity> getAll();


    void deleteRecord(PurseEntity record);

}

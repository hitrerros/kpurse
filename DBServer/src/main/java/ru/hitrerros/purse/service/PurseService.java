package ru.hitrerros.purse.service;

import ru.hitrerros.purse.model.PurseEntity;

import java.time.LocalDate;
import java.util.List;

public interface PurseService {

    PurseEntity getRecord(long id);

    PurseEntity saveRecord(PurseEntity record);

    void deleteRecord(PurseEntity record);

    List<PurseEntity> getRangeOfRecords(LocalDate from, LocalDate to);

}

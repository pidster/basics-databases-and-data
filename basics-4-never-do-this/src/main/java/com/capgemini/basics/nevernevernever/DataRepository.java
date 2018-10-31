package com.capgemini.basics.nevernevernever;

import java.util.List;

/**
 *
 */
public interface DataRepository {

    void create(ImportantData importantData) throws DataException;

    ImportantData findByName(String name) throws DataException;

    List<ImportantData> findAll() throws DataException;

}

package com.capgemini.basics.springdata;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 */
public interface EepRepository extends CrudRepository<Eep, Long> {

    @Query("SELECT id, name, created, updated FROM eep WHERE upper(name) like '%' || upper(:name) || '%' ")
    Eep findByName(@Param("name") String name);

}

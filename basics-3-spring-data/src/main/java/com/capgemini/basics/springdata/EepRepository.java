package com.capgemini.basics.springdata;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * There's no implementation!
 *
 * Creating database handling code is so predictable and repetitive, that
 * it's possible for Spring Data to create all of the code for us.
 */
public interface EepRepository extends CrudRepository<Eep, Long> {

    @Query("SELECT id, name, created, updated FROM eep WHERE upper(name) like '%' || upper(:name) || '%' ")
    Eep findByName(@Param("name") String name);

}

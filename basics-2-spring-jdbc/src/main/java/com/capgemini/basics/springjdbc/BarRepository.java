package com.capgemini.basics.springjdbc;

import java.util.List;

/**
 *
 */
public interface BarRepository {

    void create(Bar bar);

    Bar findByName(String name);

    List<Bar> findAll();

}

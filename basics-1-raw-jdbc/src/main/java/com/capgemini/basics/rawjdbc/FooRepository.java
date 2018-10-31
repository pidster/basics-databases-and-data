package com.capgemini.basics.rawjdbc;

import java.io.IOException;
import java.util.List;

/**
 *
 */
public interface FooRepository {

    void create(Foo foo) throws FooException;

    Foo findByName(String name) throws FooException;

    List<Foo> findAll() throws FooException;

}

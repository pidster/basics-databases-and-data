package com.capgemini.basics.rawjdbc;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class FooRepositoryTests {

    private static FooRepository fooRepository;

    @Test
    public void test1() throws FooException {

        String name = "Bob";
        Foo foo = new Foo();
        foo.setName(name);
        foo.setCreated(new Date());
        foo.setUpdated(new Date());

        fooRepository.create(foo);
    }

    @Test
    public void test2() throws FooException {

        String name = "Bob";
        Foo foo = fooRepository.findByName(name);

        assertEquals(name, foo.getName());
    }

    @Test
    public void test3() throws FooException {

        List<Foo> fooList = fooRepository.findAll();

        assertEquals(1, fooList.size());
    }

    @Test
    public void test4(){

        String name = "Nope!";

        assertThrows(FooException.class, () ->
            fooRepository.findByName(name)
        );
    }


    @BeforeAll
    public static void setup() throws ClassNotFoundException {

        // Setup for the database.
        // We're using an embedded database just to keep the example as simple as possible.
        Class.forName("org.hsqldb.jdbc.JDBCDriver");

        String jdbc_url = "jdbc:hsqldb:mem:testdb";
        String username = "SA";
        String password = "";

        Properties properties = new Properties();
        properties.setProperty("username", username);
        properties.setProperty("password", password);

        fooRepository = new FooRepositoryImpl(jdbc_url, properties);
    }
}

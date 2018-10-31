package com.capgemini.basics.nevernevernever;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class DataRepositoryTests {

    private static DataRepository dataRepository;

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

        dataRepository = new DataRepositoryImpl(jdbc_url, properties);
    }

    @Test
    public void test1() throws DataException {

        String name = "Bob";
        ImportantData importantData = new ImportantData();
        importantData.setName(name);
        importantData.setCreated(new Date());
        importantData.setUpdated(new Date());

        dataRepository.create(importantData);
    }

    @Test
    public void test2() throws DataException {

        String name = "Bob";
        ImportantData importantData = dataRepository.findByName(name);

        assertEquals(name, importantData.getName());
    }

    @Test
    public void test3() throws DataException {

        List<ImportantData> importantDataList = dataRepository.findAll();

        assertEquals(1, importantDataList.size());
    }

    @Test
    public void test4() throws DataException {

        String name = "Nope!";
        ImportantData data = dataRepository.findByName(name);

        assertNull(data);
    }

    @Test
    public void test5() throws DataException {

        // It's a SQL injection attack!
        String name = "Bob'; DROP TABLE important_data; --";


        // TODO Uncomment the following lines: what do you expect to happen?

        ImportantData importantData = dataRepository.findByName(name);
        // assertEquals(name, importantData.getName());
    }
}

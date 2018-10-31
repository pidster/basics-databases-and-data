package com.capgemini.basics.springjdbc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BarConfiguration.class)
public class BarRepositoryTests {

    @Autowired
    BarRepository barRepository;

    @Test
    public void test1() {

        String name = "Bob";
        Bar bar = new Bar();
        bar.setName(name);
        bar.setCreated(new Date());
        bar.setUpdated(new Date());

        barRepository.create(bar);
    }

    @Test
    public void test2() {

        String name = "Bob";
        Bar foo = barRepository.findByName(name);

        assertEquals(name, foo.getName());
    }

    @Test
    public void test3() {

        List<Bar> fooList = barRepository.findAll();

        assertEquals(1, fooList.size());
    }

    @Test
    public void test4() {

        String name = "Nobody of this name!";

        assertThrows(Exception.class, () ->
            barRepository.findByName(name)
        );
    }
}

package com.capgemini.basics.springdata;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
// @Transactional
@ContextConfiguration(classes = EepConfiguration.class)
public class EepRepositoryTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(EepRepositoryTests.class);


    @Autowired
    EepRepository eepRepository;

    @Test
    public void test1() {

        String name = "Bob";
        Eep eep = new Eep();
        eep.setName(name);
        eep.setCreated(LocalDate.now());
        eep.setUpdated(LocalDate.now());

        Eep saved = eepRepository.save(eep);

        assertNotNull(saved.getId());
        assertEquals(name, eep.getName());
    }

    @Test
    public void test2() {

        String name = "Bob";
        Eep eep = eepRepository.findByName(name);

        assertNotNull(eep);
        assertEquals(name, eep.getName());
    }

    @Test
    public void test3() {

        long count = eepRepository.count();

        assertEquals(1L, count);
    }

    @Test
    public void test4() {

        String name = "Nobody of this name!";
        Eep eep = eepRepository.findByName(name);

        // the query won't match, so we expect a null here
        assertNull(eep);
    }
}

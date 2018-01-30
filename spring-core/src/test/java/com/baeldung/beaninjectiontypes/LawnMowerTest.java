package com.baeldung.beaninjectiontypes;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class LawnMowerTest {

    @Autowired
    LawnMower lawnMower;

    @Test
    public void givenAutowired_whenOnField_thenConstructorInjected() {
        assertNotNull(lawnMower);
        assertNotNull(lawnMower.getEngine());
    }
}

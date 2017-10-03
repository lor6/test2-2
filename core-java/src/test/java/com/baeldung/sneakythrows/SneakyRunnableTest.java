package com.baeldung.sneakythrows;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class SneakyRunnableTest {

    @Test
    public void whenCallSneakyRunnableMethod_thenThrowException() {
        try {
            new SneakyRunnable().run();
        } catch (Exception e) {
            assertEquals(InterruptedException.class, e.getStackTrace());
        }
    }
}

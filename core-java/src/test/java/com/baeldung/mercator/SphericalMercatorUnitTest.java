package com.baeldung.mercator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.TestCase.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class SphericalMercatorUnitTest {

    @Test
    public void giventThatTheInputIs22_whenXAxisProjectionIsCalled_thenTheResultIsTheCorrectOne() {
        Mercator mercator = new SphericalMercator();
        double result = mercator.xAxisProjection(22);
        assertEquals(result, 2511525.234845713);
    }

    @Test
    public void giventThatTheInputIs44_whenYAxisProjectionIsCalled_thenTheResultIsTheCorrectOne() {
        Mercator mercator = new SphericalMercator();
        double result = mercator.yAxisProjection(44);
        assertEquals(result, 4898057.594904037);
    }
}

package com.baeldung.designpatterns;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.baeldung.designpatterns.adapter.AstonMartin;
import com.baeldung.designpatterns.adapter.BugattiVeyron;
import com.baeldung.designpatterns.adapter.McLaren;
import com.baeldung.designpatterns.adapter.Movable;
import com.baeldung.designpatterns.adapter.MovableAdapter;
import com.baeldung.designpatterns.adapter.MovableAdapterImpl;

public class AdapterPatternIntegrationTest {
    @Test
    public void givenMovableAdapter_WhenConvertingMPHToKMPH_thenSuccessfullyConverted() {
        Movable bugattiVeyron = new BugattiVeyron();
        MovableAdapter bugattiVeyronAdapter = new MovableAdapterImpl(bugattiVeyron);
        assertEquals(bugattiVeyronAdapter.getSpeed(), 431.30312, 0.00001);

        Movable mcLaren = new McLaren();
        MovableAdapter mcLarenAdapter = new MovableAdapterImpl(mcLaren);
        assertEquals(mcLarenAdapter.getSpeed(), 387.85094, 0.00001);

        Movable astonMartin = new AstonMartin();
        MovableAdapter astonMartinAdapter = new MovableAdapterImpl(astonMartin);
        assertEquals(astonMartinAdapter.getSpeed(), 354.0548, 0.00001);
    }
}


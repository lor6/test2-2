package com.baeldung.string.interview;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LocaleUnitTest {
    @Test
    public void whenUsingLocal_thenCorrectResultsForDifferentLocale() {
        Locale usLocale = Locale.US;
        BigDecimal number = new BigDecimal(102_300.456d);
         
        NumberFormat usNumberFormat = NumberFormat.getCurrencyInstance(usLocale); 
        assertEquals(usNumberFormat.format(number), "$102,300.46");
    }
}

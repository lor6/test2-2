package com.baeldung.convert;

import org.joda.time.Instant;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

public class ConvertDateTime {

    public static void main(String[] args) {

        java8();
        joda();
        Date date = coreDate();
        calendar(date);
    }

    private static Date coreDate() {
        Date date = new Date();

        System.out.println("Date - Time in milliseconds : " + date.getTime());

        return date;
    }

    private static void calendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        System.out.println("Calender - Time in milliseconds : " + calendar.getTimeInMillis());
    }

    private static void joda() {
        org.joda.time.DateTime jodaDateTime = new org.joda.time.DateTime();
        long delta = jodaDateTime.getMillis();
        System.out.println("Joda - Time in milliseconds : " + delta);

        Instant jodaInstant = Instant.now();
        System.out.println("Joda - Instant in milliseconds : " + jodaInstant.getMillis());
    }

    private static void java8() {
        long instantMillis = java.time.Instant.now().toEpochMilli();

        LocalDateTime localDateTime = LocalDateTime.now();
        ZoneId id = ZoneId.systemDefault();
        ZonedDateTime zdt = ZonedDateTime.of(localDateTime, id);
        System.out.println("Java 8 - Time in milliseconds : " + zdt.toInstant().toEpochMilli());
    }
}

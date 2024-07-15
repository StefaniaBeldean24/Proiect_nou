package com.internship.backend.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NewDateTest {

    @Test
    void testEquals() {

        NewDate newDate1 = new NewDate(30,12,5,3,2024);
        NewDate newDate2 = new NewDate(30,12,5,3,2024);
        NewDate newDate3 = new NewDate(0,14,20,2,2024);

        assertEquals(newDate1, newDate2);
        assertNotEquals(newDate1, newDate3);
    }

    @Test
    void isAfter() {

        NewDate newDate1 = new NewDate(30,12,5,3,2024);
        NewDate newDate2 = new NewDate(30,10,5,3,2024);
        NewDate newDate3 = new NewDate(0,14,20,2,2024);

        assertTrue(newDate1.isAfter(newDate2));
        assertTrue(newDate2.isAfter(newDate3));
        assertFalse(newDate3.isAfter(newDate2));
    }

    @Test
    void isBefore() {

        NewDate newDate1 = new NewDate(30,12,5,3,2024);
        NewDate newDate2 = new NewDate(30,10,5,3,2024);
        NewDate newDate3 = new NewDate(0,14,20,2,2024);

        assertTrue(newDate3.isBefore(newDate2));
        assertTrue(newDate3.isBefore(newDate1));
        assertTrue(newDate2.isBefore(newDate1));
        assertFalse(newDate1.isBefore(newDate2));
    }

    @Test
    void testSameDateComparison(){

        NewDate newDate1 = new NewDate(30,12,5,3,2024);

        assertFalse(newDate1.isAfter(newDate1));
        assertFalse(newDate1.isBefore(newDate1));
    }

    @Test
    void getMinute() {

        NewDate newDate1 = new NewDate(30,12,5,3,2024);
        NewDate newDate2 = new NewDate(30,10,5,3,2024);
        NewDate newDate3 = new NewDate(0,14,20,2,2024);

        assertEquals(newDate1.getMinute(), 30);
        assertEquals(newDate2.getMinute(), 30);
        assertEquals(newDate3.getMinute(), 0);
        assertNotEquals(newDate1.getMinute(), 12);
    }

    @Test
    void getHour() {

        NewDate newDate1 = new NewDate(30,12,5,3,2024);
        NewDate newDate2 = new NewDate(30,10,5,3,2024);
        NewDate newDate3 = new NewDate(0,14,20,2,2024);

        assertEquals(newDate1.getHour(), 12);
        assertEquals(newDate2.getHour(), 10);
        assertEquals(newDate3.getHour(), 14);
        assertNotEquals(newDate1.getHour(), 15);
    }

    @Test
    void getDay() {

        NewDate newDate1 = new NewDate(30,12,5,3,2024);
        NewDate newDate2 = new NewDate(30,10,5,3,2024);
        NewDate newDate3 = new NewDate(0,14,20,2,2024);

        assertEquals(newDate1.getDay(), 5);
        assertEquals(newDate2.getDay(), 5);
        assertEquals(newDate3.getDay(), 20);
        assertNotEquals(newDate1.getDay(), 15);
    }

    @Test
    void getMonth() {

        NewDate newDate1 = new NewDate(30,12,5,3,2024);
        NewDate newDate2 = new NewDate(30,10,5,3,2024);
        NewDate newDate3 = new NewDate(0,14,20,2,2024);

        assertEquals(newDate1.getMonth(), 3);
        assertEquals(newDate2.getMonth(), 3);
        assertEquals(newDate3.getMonth(), 2);
        assertNotEquals(newDate1.getMonth(), 15);
    }

    @Test
    void getYear() {

        NewDate newDate1 = new NewDate(30,12,5,3,2024);
        NewDate newDate2 = new NewDate(30,10,5,3,2028);
        NewDate newDate3 = new NewDate(0,14,20,2,2025);

        assertEquals(newDate1.getYear(), 2024);
        assertEquals(newDate2.getYear(), 2028);
        assertEquals(newDate3.getYear(), 2025);
        assertNotEquals(newDate1.getYear(), 2003);
    }

    @Test
    void setMinute() {

        NewDate newDate1 = new NewDate(30,12,5,3,2024);
        NewDate newDate2 = new NewDate(30,10,5,3,2028);
        NewDate newDate3 = new NewDate(0,14,20,2,2025);

        newDate1.setMinute(10);
        newDate2.setMinute(15);
        newDate3.setMinute(7);

        assertEquals(newDate1.getMinute(), 10);
        assertEquals(newDate2.getMinute(), 15);
        assertEquals(newDate3.getMinute(), 7);
        assertNotEquals(newDate1.getMinute(), 12);
    }

    @Test
    void setHour() {

        NewDate newDate1 = new NewDate(30,12,5,3,2024);
        NewDate newDate2 = new NewDate(30,10,5,3,2028);
        NewDate newDate3 = new NewDate(0,14,20,2,2025);

        newDate1.setHour(10);
        newDate2.setHour(15);
        newDate3.setHour(16);

        assertEquals(newDate1.getHour(), 10);
        assertEquals(newDate2.getHour(), 15);
        assertEquals(newDate3.getHour(), 16);
        assertNotEquals(newDate1.getHour(), 12);
    }

    @Test
    void setDay() {

        NewDate newDate1 = new NewDate(30,12,5,3,2024);
        NewDate newDate2 = new NewDate(30,10,5,3,2028);
        NewDate newDate3 = new NewDate(0,14,20,2,2025);

        newDate1.setDay(10);
        newDate2.setDay(15);
        newDate3.setDay(7);

        assertEquals(newDate1.getDay(), 10);
        assertEquals(newDate2.getDay(), 15);
        assertEquals(newDate3.getDay(), 7);
        assertNotEquals(newDate1.getDay(), 5);
    }

    @Test
    void setMonth() {

        NewDate newDate1 = new NewDate(30,12,5,3,2024);
        NewDate newDate2 = new NewDate(30,10,5,3,2028);
        NewDate newDate3 = new NewDate(0,14,20,2,2025);

        newDate1.setMonth(10);
        newDate2.setMonth(12);
        newDate3.setMonth(8);

        assertEquals(newDate1.getMonth(), 10);
        assertEquals(newDate2.getMonth(), 12);
        assertEquals(newDate3.getMonth(), 8);
        assertNotEquals(newDate1.getMonth(), 3);
    }

    @Test
    void setYear() {

        NewDate newDate1 = new NewDate(30,12,5,3,2024);
        NewDate newDate2 = new NewDate(30,10,5,3,2028);
        NewDate newDate3 = new NewDate(0,14,20,2,2025);

        newDate1.setYear(2003);
        newDate2.setYear(2012);
        newDate3.setYear(2020);

        assertEquals(newDate1.getYear(), 2003);
        assertEquals(newDate2.getYear(), 2012);
        assertEquals(newDate3.getYear(), 2020);
        assertNotEquals(newDate1.getYear(), 2024);
    }
}
package com.internship.backend.service;

import com.internship.backend.repository.*;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class IdGeneratorService {

    //AtomicInteger insures that increment operations ar atomic
    private final AtomicInteger counter = new AtomicInteger(1);
    //a constant that represents the maximum value the counter can reach
    private final int MAX_ID = 999;

    public synchronized int getCurrentId() {
        //automatically increments the current value of the counter by 1 and returns the previous value
        int id = counter.getAndIncrement();
        if (id > MAX_ID) {
            counter.set(0);
            id = counter.getAndIncrement();
        }
        return id;
    }
}

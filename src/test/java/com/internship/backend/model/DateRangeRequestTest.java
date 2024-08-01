package com.internship.backend.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
class DateRangeRequestTest {

    private LocalDateTime start;
    private LocalDateTime end;
    private DateRangeRequest dateRangeRequest;

    @BeforeEach
    void setUp() {
        start = LocalDateTime.of(2023, 7, 1, 10, 0);
        end = LocalDateTime.of(2023, 7, 1, 12, 0);
        dateRangeRequest = new DateRangeRequest(start, end);
    }

    @AfterEach
    void tearDown() {
        dateRangeRequest = null;
    }

    @Test
    void start() {
        assertEquals(start, dateRangeRequest.start());
    }

    @Test
    void end() {
        assertEquals(end, dateRangeRequest.end());
    }
}
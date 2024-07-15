package com.internship.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//
//@Setter
//@Getter
//@AllArgsConstructor
//@NoArgsConstructor
//public class DateRangeRequest {
//
//    private NewDate startDate;
//    private NewDate endDate;
//}

public record DateRangeRequest(NewDate start, NewDate end) {}

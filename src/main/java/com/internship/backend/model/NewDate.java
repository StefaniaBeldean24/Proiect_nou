package com.internship.backend.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewDate {

    private int minute;
    private int hour;
    private int day;
    private int month;
    private int year;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewDate date = (NewDate) o;
        return minute == date.minute && hour == date.hour && day == date.day && month == date.month && year == date.year;
    }

    public boolean isAfter(Object o){
        if (this == o) return false;
        if (o == null || getClass() != o.getClass()) return false;

        NewDate date = (NewDate) o;
        if (year > date.year) return true;
        if (year == date.year) {
            if (month > date.month) return true;
            if (month == date.month) {
                if (day > date.day) return true;
                if (day == date.day) {
                    if (hour > date.hour) return true;
                    if (hour == date.hour) {
                        return minute > date.minute;
                    }
                }
            }
        }
        return false;
    }

    public boolean isBefore(Object o){
        if (this == o) return false;
        if (o == null || getClass() != o.getClass()) return false;

        NewDate date = (NewDate) o;
        if (year < date.year) return true;
        if (year == date.year) {
            if (month < date.month) return true;
            if (month == date.month) {
                if (day < date.day) return true;
                if (day == date.day) {
                    if (hour < date.hour) return true;
                    if (hour == date.hour) {
                        return minute < date.minute;
                    }
                }
            }
        }
        return false;
    }

}


